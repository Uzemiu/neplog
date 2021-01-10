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
public class LocalStorage extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Column(name = "name")
    @ColumnDefault("'file'")
    private String name;

    @Column(name = "virtual_path", nullable = false)
    private String virtualPath;

    @Column(name = "local_path", nullable = false)
    private String localPath;

    @Column(name = "size")
    @ColumnDefault("0")
    private Long size;

    @Column(name = "hash")
    @ColumnDefault("''")
    private String hash;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(!StringUtils.hasText(name)){
            name = "file";
        }
        if(size == null){
            size = 0L;
        }
        if(hash == null){
            hash = "";
        }
    }
}
