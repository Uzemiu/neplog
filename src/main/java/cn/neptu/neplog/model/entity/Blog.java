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
public class Blog extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(31) not null")
    private String name;

    @Column(name = "background_image", columnDefinition = "varchar(1023) default ''")
    private String backgroundImage;

    @Column(name = "template", columnDefinition = "varchar(1023) default ''")
    private String template;

    @Column(name = "visits", columnDefinition = "int default 0")
    private Integer visits;

    @Override
    public void prePersist(){
        super.prePersist();
        if(backgroundImage == null){
            backgroundImage = "";
        }
        if(visits == null){
            visits = 1;
        }
    }
}
