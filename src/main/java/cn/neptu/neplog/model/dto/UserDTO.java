package cn.neptu.neplog.model.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String username;

    private String nickname;

    private String avatar;

    private Integer level;
}
