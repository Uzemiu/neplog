package cn.neptu.neplog.service;

import cn.neptu.neplog.model.params.RegisterParam;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.service.base.CrudService;

import java.util.Optional;

public interface UserService extends CrudService<User, String> {

    Optional<User> findById(String id);

    Optional<User> getByLevel(Integer level);

    User register(RegisterParam param);

    User save(User user);

    Optional<User> findByUsername(String username);
}
