package cn.neptu.neplog.handler;

import cn.neptu.neplog.exception.UploadFailureException;
import cn.neptu.neplog.model.option.ThumbnailOption;
import cn.neptu.neplog.model.option.UploadFileOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Component
public class LocalFileHandler implements FileHandler{

//    @Value("${neplog.uploaded-file.root:uploads}")
    private String localFileRootPath;

    private String host = "";

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
        String extension = FileHandler.getFileExtension(filename);
        String baseName = FileHandler.getFileBaseName(filename);

        File origin = new File(pathPrefix + filename);
        ensurePathExists(origin.getParent());
        try {
            // if not use getAbsoluteFile() ide will set to tomcat's path
            file.transferTo(origin.getAbsoluteFile());
            log.info("Successfully save file: '{}' to local storage: '{}'",filename,origin.getAbsolutePath());

            // generate and save thumbnail
            ThumbnailOption to = option.getThumbnail();
            if(to != null && to.isGenerate()
                    && FileHandler.isImageType(file.getContentType()) && !extension.equals("svg")){

                File thumbFile = new File(pathPrefix + baseName + THUMBNAIL_SUFFIX + "." + extension);
                BufferedImage originImage = ImageIO.read(origin.getAbsoluteFile());
                BufferedImage result = to.getGenerateStrategy().process(originImage,to.getWidth(),to.getHeight());
                ImageIO.write(result,extension,thumbFile.getAbsoluteFile());
                log.info("Successfully save thumbnail image: '{}'",thumbFile.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new UploadFailureException("上传文件失败，未知错误");
        }
        return host + origin.getPath().replaceAll("\\\\","/");
    }

    @Override
    public boolean delete(String path) {
        if(path.startsWith(host)){
            path = path.substring(path.indexOf('/') + 1);
        }
        File file = new File(path);
        // delete thumbnail by default
        StringBuilder sb = new StringBuilder(path);
        int i = path.lastIndexOf('.');
        sb.insert(i,THUMBNAIL_SUFFIX);
        File thumb = new File(sb.toString());
        log.info("Trying to delete file: " + file.getAbsolutePath());
        deleteFile(thumb);
        return deleteFile(file);
    }

    private boolean deleteFile(File f){
        return f.exists() && f.delete();
    }

}
