package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class FriendBlog extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "blog_id", columnDefinition = "int not null")
    private Integer blogId;

    @Column(name = "url", columnDefinition = "varchar(1023) not null")
    private Integer url;

    @Column(name = "name", columnDefinition = "varchar(31) not null")
    private String name;

    @Column(name = "intro", columnDefinition = "varchar(255) default ''")
    private String intro;

    @Column(name = "avatar",columnDefinition = "varchar(1023) default ''")
    private String avatar;

}
