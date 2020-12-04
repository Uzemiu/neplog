package cn.neptu.neplog.handler;

import cn.neptu.neplog.model.option.UploadFileOption;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public interface FileHandler {

    MediaType IMAGE_TYPE = MediaType.valueOf("image/*");

    String THUMBNAIL_SUFFIX = "-thumb";

    static boolean isImageType(String mediaType) {
        return mediaType != null && IMAGE_TYPE.includes(MediaType.valueOf(mediaType));
    }

    static String getFileExtension(String file){
        int i = file.lastIndexOf(".");
        return i < 0 ? "" : file.substring(i + 1);
    }

    static String getFileBaseName(String file){
        int i = file.lastIndexOf(".");
        return i < 0 ? file : file.substring(0,i);
    }

    /**
     * Save the file to local storage and return the path that can be mapped to the file
     * @return the path that can be mapped to the file or null if failure to save
     * @param option the option of file storage
     */
    String upload(MultipartFile file, UploadFileOption option);

    boolean delete(String path);
}
