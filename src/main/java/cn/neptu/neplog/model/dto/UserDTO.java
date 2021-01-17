package cn.neptu.neplog.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;

    private String nickname;

    private String avatar;

    private String site;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer level;
}
