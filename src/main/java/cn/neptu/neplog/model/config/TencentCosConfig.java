package cn.neptu.neplog.model.config;

import cn.neptu.neplog.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class TencentCosConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "secret_id")
    @ColumnDefault("''")
    private String secretId;

    @Column(name = "secret_key")
    @ColumnDefault("''")
    private String secretKey;

    @Column(name = "region")
    @ColumnDefault("''")
    private String region;

    @Column(name = "bucket_name")
    @ColumnDefault("''")
    private String bucketName;

    @Override
    protected void prePersist() {
        super.prePersist();
    }
}
