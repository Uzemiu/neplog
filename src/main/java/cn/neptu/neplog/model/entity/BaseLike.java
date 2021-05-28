package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@MappedSuperclass
public class BaseLike extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * UserId or ip
     */
    @Column(name = "identity",nullable = false)
    private String identity;

    @NotNull(message = "目标ID不能为空")
    @Column(name = "target_id",nullable = false)
    private Long targetId;

    /**
     * -1 反对
     * 1 赞成
     */
    @NotNull(message = "点赞信息不能为空")
    @Column(name = "opinion",nullable = false)
    private Integer opinion;

    public void setOpinion(Integer opinion) {
        if(opinion == null) this.opinion = 0;
        else if(opinion < -1) this.opinion = -1;
        else if(opinion > 1) this.opinion = 1;
        else this.opinion = opinion;
    }
}
