package cn.neptu.neplog.service;

import cn.hutool.core.util.ArrayUtil;
import cn.neptu.neplog.model.dto.StorageDTO;
import cn.neptu.neplog.model.entity.Storage;
import cn.neptu.neplog.model.query.StorageQuery;
import cn.neptu.neplog.model.support.UploadFileOption;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FileService {

    MediaType IMAGE_TYPE = MediaType.valueOf("image/*");

    String THUMBNAIL_SUFFIX = "_thumb";

    String ORIGIN_SUFFIX = "_origin";

    static StringBuilder generateBaseFileName(MultipartFile file, UploadFileOption option){
        StringBuilder sb = new StringBuilder();
        return sb.append(ArrayUtil.join(option.getPath(),"/","","/"))
                .append(System.currentTimeMillis())
                .append('-')
                .append(StringUtils.hasText(file.getOriginalFilename())
                        ? file.getOriginalFilename()
                        : "empty");
    }

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
    Storage upload(MultipartFile file, UploadFileOption option);

    boolean delete(Storage storage);

}
