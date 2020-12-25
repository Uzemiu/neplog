package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.UploadFailureException;
import cn.neptu.neplog.model.support.UploadFileOption;
import cn.neptu.neplog.service.FileService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Value("${neplog.file.root:uploads}")
    private String localFileRootPath;

    public static void ensurePathExists(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public String upload(MultipartFile file, UploadFileOption option) {
        Assert.notNull(option,"UploadFileOption must not be null.");
        // use currentTimeMillis + originalFilename as new filename
        String pathPrefix = Paths.get(localFileRootPath,option.getPath()) + File.separator + System.currentTimeMillis();
        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("empty");
        String extension = FileService.getFileExtension(filename);
        String baseName = FileService.getFileBaseName(filename);

        boolean scalable = FileService.isImageType(file.getContentType()) && !extension.equals("svg");
        boolean compressed = scalable && (option.getCompress() == null || option.getCompress());
        String baseFilename = pathPrefix + baseName;
        File origin = new File(baseFilename + (compressed ? ORIGIN_SUFFIX : "") + "." + extension);
        ensurePathExists(origin.getParent());
        try {
            // if not use getAbsoluteFile() ide will set to tomcat's path
            file.transferTo(origin.getAbsoluteFile());
            log.info("Successfully save file: '{}' to local storage: '{}'",filename,origin.getAbsolutePath());

            BufferedImage compressedImage = null;
            if(compressed){
                // save compressed image
                compressedImage = Thumbnails.of(origin.getAbsoluteFile())
                        .scale(1.0)
                        .asBufferedImage();
                ImageIO.write(compressedImage,extension,new File(baseFilename + "." + extension).getAbsoluteFile());
            }
            if(option.getThumbnail() != null && option.getThumbnail()){
                (compressedImage == null
                        ? Thumbnails.of(origin)
                        : Thumbnails.of(compressedImage))
                        .size(option.getWidth(),option.getHeight())
                        .toFile(new File(baseFilename + THUMBNAIL_SUFFIX + "." + extension));
            }
        } catch (IOException e) {
            log.error("上传文件失败，未知错误",e);
            throw new UploadFailureException("上传文件失败，未知错误");
        }
        return "/" + baseFilename.replaceAll("\\\\","/") + "." + extension;
    }

    @Override
    public boolean delete(String path) {
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

}
