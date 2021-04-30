package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name",length = 31,nullable = false)
    private String name;

    @Column(name = "parent_id")
    private Integer parentId;

    @OneToMany(mappedBy = "category")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<Article> articles;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(name == null || "".equals(name)){
            name = "未命名";
        }
    }

}
