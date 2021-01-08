package cn.neptu.neplog.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.neptu.neplog.config.common.UploadFileConfig;
import cn.neptu.neplog.exception.UploadFailureException;
import cn.neptu.neplog.model.entity.LocalStorage;
import cn.neptu.neplog.model.support.UploadFileOption;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.service.LocalStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("fileService")
public class FileServiceImpl implements FileService {

    private final UploadFileConfig uploadFileConfig;
    private final LocalStorageService localStorageService;

    public static void ensurePathExists(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public String upload(MultipartFile file, UploadFileOption option) {
        LocalStorage localStorage = uploadLocal(file, option);
        localStorageService.create(localStorage);
        return localStorage.getVirtualPath();
    }

    @Override
    public boolean delete(String path) {
        path = path.replace(uploadFileConfig.getVirtual(),uploadFileConfig.getRoot());
        File compressed = new File(path);
        int i = path.lastIndexOf('.');
        File thumb = new File(new StringBuilder(path).insert(i,THUMBNAIL_SUFFIX).toString());
        File origin = new File(new StringBuilder(path).insert(i,ORIGIN_SUFFIX).toString());
        if(deleteFile(thumb)){
            log.info("File deleted: {}",thumb.getAbsoluteFile());
        }
        if(deleteFile(origin)){
            log.info("File deleted: {}",origin.getAbsoluteFile());
        }
        if(deleteFile(compressed)){
            log.info("File deleted: {}",compressed.getAbsoluteFile());
        }
        return true;
    }

    private boolean deleteFile(File f){
        return f.exists() && f.delete();
    }

    private LocalStorage uploadLocal(MultipartFile file, UploadFileOption option) {
        Assert.notNull(file, "File must not be null.");
        Assert.notNull(option,"UploadFileOption must not be null.");
        LocalStorage localStorage = new LocalStorage();

        // use currentTimeMillis + '-' + originalFilename as new filename
        // 包含前缀'/'
        String pathPrefix = ArrayUtil.join(option.getPath(),"/","/","/") + System.currentTimeMillis() + "-";

        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("empty");
        String baseName = FileService.getFileBaseName(filename);
        String extension = FileService.getFileExtension(filename);

        boolean scalable = FileService.isImageType(file.getContentType()) && !extension.equals("svg");
        boolean compressed = scalable && (option.getCompress() == null || option.getCompress());
        String baseFilename = uploadFileConfig.getRoot() + pathPrefix + baseName;
        File origin = new File(baseFilename + (compressed ? ORIGIN_SUFFIX : "") + "." + extension).getAbsoluteFile();
        ensurePathExists(origin.getParent());
        try {
            // if not use getAbsoluteFile() ide will set to tomcat's path
            file.transferTo(origin);
            localStorage.setHash(DigestUtils.md5DigestAsHex(new FileInputStream(origin)));
            localStorage.setLocalPath(origin.getAbsolutePath());
            log.info("Successfully save file: '{}' to local storage: '{}'",filename,origin.getAbsolutePath());

            BufferedImage compressedImage = null;
            if(compressed){
                // save compressed image
                compressedImage = Thumbnails.of(origin)
                        .scale(1.0)
                        .asBufferedImage();
                // TODO formatName为png时会反向压缩?，得去学习一个
                File compressedFile = new File(baseFilename + "." + extension).getAbsoluteFile();
                ImageIO.write(compressedImage,"jpg",compressedFile);
            }
            if(option.getThumbnail() != null && option.getThumbnail()){
                (compressedImage == null
                        ? Thumbnails.of(origin)
                        : Thumbnails.of(compressedImage))
                        .size(option.getWidth(),option.getHeight())
                        .toFile(new File(baseFilename + THUMBNAIL_SUFFIX + "." + extension));
            }
        } catch (IOException e) {
            log.error("上传文件失败",e);
            throw new UploadFailureException(e.getMessage());
        }

        String virtualPath = uploadFileConfig.getVirtual() + pathPrefix + baseName + "." + extension;
        localStorage.setName(filename);
        localStorage.setVirtualPath(virtualPath);
        localStorage.setSize(file.getSize());
        return localStorage;
    }

}
