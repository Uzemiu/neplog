package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Storage extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "filename", length = 255, nullable = false)
    private String filename;

    @Column(name = "type")
    @ColumnDefault("'file'")
    private String type;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "virtual_path", nullable = false)
    private String virtualPath;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "size")
    @ColumnDefault("0")
    private Long size;

    @Column(name = "hash")
    @ColumnDefault("''")
    private String hash;

    @Column(name = "compressed")
    @ColumnDefault("'")
    private String compressed;

    @Column(name = "thumbnail")
    @ColumnDefault("''")
    private String thumbnail;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(!StringUtils.hasText(type)){
            type = "file";
        }
        if(size == null){
            size = 0L;
        }
        if(hash == null){
            hash = "";
        }
        if(compressed == null){
            compressed = "";
        }
        if(thumbnail == null){
            thumbnail = "";
        }
    }
}
