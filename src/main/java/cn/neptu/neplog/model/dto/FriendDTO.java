package cn.neptu.neplog.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
public class FriendDTO {

    private Long id;

    @NotBlank(message = "名称不能为空")
    @Length(max = 127,message = "博主名称须在127个字符以内")
    private String name;

    @NotBlank(message = "博客链接不能为空")
    @Length(max = 1023,message = "博客链接须在1023个字符以内")
    private String link;

    @Length(max = 1023,message = "头像链接须在1023个字符以内")
    private String avatar;

    @Length(max = 255,message = "个人介绍须在255个字符以内")
    private String introduction;

    @Range(min = 0, max = 1,message = "状态只能是0(待审核)或1(公开)")
    private Integer status;

    private String team;
}
