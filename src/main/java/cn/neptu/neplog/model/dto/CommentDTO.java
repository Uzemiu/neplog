package cn.neptu.neplog.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CommentDTO {

    private Long id;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String avatar;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @Length(max = 1023, message = "博客链接长度不能超过1023")
    private String link;

    @Length(max = 255, message = "邮箱长度不能超过1023")
    private String email;

    private String userId;

    @NotNull(message = "评论文章不能为空")
    private Long articleId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long parentId;

    private Long likes;

    private String userAgent;

    private String operatingSystem;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String ipAddress;

    private ArticleDTO article;

    private List<CommentDTO> children;

    public CommentDTO(){
        this.children = new ArrayList<>();
    }

}
