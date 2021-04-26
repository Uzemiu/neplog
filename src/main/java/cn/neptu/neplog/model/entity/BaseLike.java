package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "target_id",nullable = false)
    private Long targetId;

    /**
     * 0 反对
     * 1 赞成
     */
    @Column(name = "opinion",nullable = false)
    private Integer opinion;
}
