package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy="uuid")
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "username",length = 32,unique = true,nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "nickname",length = 32,unique = true, nullable = false)
    private String nickname;

    @Column(name = "email",length = 127)
    @ColumnDefault("''")
    private String email;

    @Column(name = "avatar",length = 1023)
    @ColumnDefault("''")
    private String avatar;

    @Column(name = "level")
    @ColumnDefault("1")
    private Integer level;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(email == null){
            email = "";
        }
        if(avatar == null){
            avatar = "";
        }
        if(level == null){
            level = 1;
        }
    }
}
