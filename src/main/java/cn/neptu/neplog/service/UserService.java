package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.UserDTO;
import cn.neptu.neplog.model.params.LoginParam;
import cn.neptu.neplog.model.params.RegisterParam;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.params.ResetPasswordParam;
import cn.neptu.neplog.service.base.CrudService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

public interface UserService extends CrudService<User, String> {

    UserDTO getOwner();

    User register(RegisterParam param);

    Optional<User> getByUsername(String username);

    User update(UserDTO userDTO);

    Map<String, Object> login(LoginParam param);

    void logout(HttpServletRequest request);

    void resetPassword(ResetPasswordParam param);

}
