package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.repository.UserRepository;
import cn.neptu.neplog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
}
