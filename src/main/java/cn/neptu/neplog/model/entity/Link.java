package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Link extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 127, nullable = false)
    private String name;

    @Column(name = "url", length = 1023, nullable = false)
    private String url;

    @Column(name = "description", length = 255)
    @ColumnDefault("''")
    private String description;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(description == null){
            description = "";
        }
    }
}
