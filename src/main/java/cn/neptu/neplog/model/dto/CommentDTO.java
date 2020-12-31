package cn.neptu.neplog.model.dto;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CommentDTO {

    private Integer id;

    @NotBlank
    private String content;

    private String avatar;

    @NotBlank
    private String nickname;

    @Length(max = 1023, message = "博客链接长度不能超过1023")
    private String link;

    @Length(max = 255, message = "邮箱长度不能超过1023")
    private String email;

    @NotNull(message = "评论文章不能为空")
    private Integer articleId;

    private Integer fatherId;

    private CommentAuthorDTO author;

    private Integer like;

    private String userAgent;

    private String operatingSystem;

    private Date createTime;

    private List<CommentDTO> children;

    public CommentDTO(){
        this.children = new ArrayList<>();
    }

}
