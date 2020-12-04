package cn.neptu.neplog.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlogParam {

    private String bloggerId;

    private String name;
}
