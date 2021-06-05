package cn.neptu.neplog.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.neptu.neplog.config.common.UploadFileConfig;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.exception.UploadFailureException;
import cn.neptu.neplog.model.entity.Storage;
import cn.neptu.neplog.model.support.UploadFileOption;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service("localFileService")
public class LocalFileServiceImpl implements FileService {

    private final UploadFileConfig uploadFileConfig;

    public static void ensurePathExists(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public Storage upload(MultipartFile file, UploadFileOption option) {
        return uploadLocal(file, option);
    }

    @Override
    public boolean delete(Storage storage) {
        deleteFile(storage.getFilePath());
        deleteFile(storage.getCompressed());
        deleteFile(storage.getThumbnail());
        return true;
    }

    private boolean deleteFile(String path){
        boolean deleted =  deleteFile(new File(path).getAbsoluteFile());
        if(deleted){
            log.info("成功删除文件：{}", path);
        }
        return deleted;
    }

    private boolean deleteFile(File f){
        return f.exists() && f.delete();
    }

    private Storage uploadLocal(MultipartFile file, UploadFileOption option) {
        Assert.notNull(file, "File must not be null.");
        Assert.notNull(option,"UploadFileOption must not be null.");

        if(file.getSize()/1024.0/1024.0 > option.getMaxSize()){
            throw new BadRequestException("文件大小不能超过 "+option.getMaxSize()+"MB");
        }

        Storage storage = new Storage();
        storage.setLocation(StorageService.LOCATION_LOCAL);

        // use currentTimeMillis + '-' + originalFilename as new filename
        // 包含前缀'/'
        String pathPrefix = ArrayUtil.join(option.getPath(),File.separator,File.separator,File.separator) + System.currentTimeMillis() + "-";

        String filename = StringUtils.hasText(file.getOriginalFilename()) ? file.getOriginalFilename() : "empty";
        String baseName = FileService.getFileBaseName(filename);
        String extension = FileService.getFileExtension(filename);

        boolean scalable = FileService.isImageType(file.getContentType()) && !extension.equals("svg");
        boolean compressed = scalable && (option.getCompress() == null || option.getCompress());

        String baseFilename = uploadFileConfig.getRoot() + pathPrefix + baseName;
        String originFilename = baseFilename + (compressed ? ORIGIN_SUFFIX : "") + "." + extension;
        File origin = new File(originFilename).getAbsoluteFile();

        ensurePathExists(origin.getParent());
        try {
            storage.setHash(DigestUtils.md5DigestAsHex(file.getInputStream()));

            file.transferTo(origin);
            storage.setFilePath(originFilename);
            log.info("成功保存文件: {}'",origin.getAbsolutePath());

            BufferedImage compressedImage = null;
            if(compressed){
                // save compressed image
                compressedImage = Thumbnails.of(origin)
                        .scale(1.0)
                        .asBufferedImage();
                // TODO formatName为png时会反向压缩，得去学习一个
                File compressedFile = new File(baseFilename + "." + extension);
                ImageIO.write(compressedImage,extension,compressedFile.getAbsoluteFile());
                storage.setCompressed(compressedFile.getPath());
            }
            if(option.getThumbnail() != null && option.getThumbnail()){
                File thumb = new File(baseFilename + THUMBNAIL_SUFFIX + "." + extension);
                (compressedImage == null
                        ? Thumbnails.of(origin)
                        : Thumbnails.of(compressedImage))
                        .size(option.getWidth(),option.getHeight())
                        .toFile(thumb.getAbsoluteFile());
                storage.setThumbnail(thumb.getPath());
            }
        } catch (IOException e) {
            log.error("上传文件至本地失败",e);
            throw new UploadFailureException(e.getMessage());
        }

        String virtualPath = (
                uploadFileConfig.getVirtual() +
                pathPrefix +
                baseName + "." + extension)
                .replaceAll("\\\\","/");
        storage.setType(option.getType());
        storage.setFilename(filename);
        storage.setVirtualPath(virtualPath);
        storage.setSize(file.getSize());

        return storage;
    }

}
