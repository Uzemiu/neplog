package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Blogger extends BaseEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy="uuid")
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "username",columnDefinition = "varchar(32) not null unique")
    private String username;

    @Column(name = "password",columnDefinition = "varchar(127) not null")
    private String password;

    @Column(name = "nickname",columnDefinition = "varchar(31) not null")
    private String nickname;

    @Column(name = "email",columnDefinition = "varchar(127) default ''")
    private String email;

    @Column(name = "description",columnDefinition = "varchar(127) default ''")
    private String description;

    @Column(name = "avatar",columnDefinition = "varchar(1023) default ''")
    private String avatar;

    @Column(name = "last_login_ip", columnDefinition = "varchar(15) default ''")
    private String lastLoginIP;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(email == null){
            email = "";
        }
        if(description == null){
            description = "";
        }
        if(avatar == null){
            avatar = "";
        }
    }
}
