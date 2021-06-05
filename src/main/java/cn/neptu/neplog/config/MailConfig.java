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
public class MailConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "host")
    @ColumnDefault("''")
    private String host;

    @Column(name = "username")
    @ColumnDefault("''")
    private String username;

    @Column(name = "password")
    @ColumnDefault("''")
    private String password;

    @Column(name = "port")
    @ColumnDefault("0")
    private Integer port;

    @Column(name = "from_user")
    @ColumnDefault("''")
    private String from;

    @Column(name = "protocol")
    @ColumnDefault("''")
    private String protocol;

    @Column(name = "encoding")
    @ColumnDefault("''")
    private String encoding;

    @Override
    protected void prePersist() {
        super.prePersist();
    }
}
