package cn.neptu.neplog.model.dto;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;

@Data
public class StorageDTO {

    private String filename;

    private String type;

    private String virtualPath;

    private String filePath;

    private Long size;

    private String hash;

}
