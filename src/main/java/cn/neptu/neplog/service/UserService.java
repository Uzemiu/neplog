package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(String id);
}
