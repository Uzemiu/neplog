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

    @Column(name = "username",columnDefinition = "varchar(32) not null unique")
    private String username;

    @Column(name = "password",nullable = false,columnDefinition = "varchar(127) not null")
    private String password;

    @Column(name = "nickname",nullable = false,columnDefinition = "varchar(31) not null")
    private String nickname;

    @Column(name = "email",columnDefinition = "varchar(127) default ''")
    private String email;

    @Column(name = "avatar",columnDefinition = "varchar(1023) default ''")
    private String avatar;

    @Column(name = "last_login_ip", columnDefinition = "varchar(15) default ''")
    private String lastLoginIP;

    @Column(name = "level")
    @ColumnDefault("1")
    private Integer level;
}
