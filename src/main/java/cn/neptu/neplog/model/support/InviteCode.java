package cn.neptu.neplog.model.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteCode {

    private String code;
    private Date expireTime;
    private String ownerId;
}
