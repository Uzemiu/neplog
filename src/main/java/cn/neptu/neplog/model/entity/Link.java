package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Link extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "blogger_id", columnDefinition = "char(32) not null")
    private String bloggerId;

    @Column(name = "name", columnDefinition = "varchar(32) not null")
    private String name;

    @Column(name = "url", columnDefinition = "varchar(1024) not null")
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Link link = (Link) o;
        return Objects.equals(bloggerId, link.bloggerId) &&
                Objects.equals(name, link.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bloggerId, name);
    }
}
