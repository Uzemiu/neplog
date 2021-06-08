package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "friend", indexes = {
        @Index(name = "friend_team", columnList = "team")})
public class Friend extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 127, nullable = false)
    private String name;

    @Column(name = "link", length = 1023, nullable = false)
    private String link;

    @Column(name = "avatar", length = 1023)
    @ColumnDefault("''")
    private String avatar;

    @Column(name = "introduction", length = 255)
    @ColumnDefault("''")
    private String introduction;

    @Column(name = "team")
    @ColumnDefault("''")
    private String team;

    /**
     * 0 待审核
     * 1 公开
     */
    @Column(name = "status")
    @ColumnDefault("0")
    private Integer status;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(introduction == null){
            introduction = "";
        }
        if(status == null){
            status = 0;
        }
        if(team == null){
            team = "";
        }
    }
}
