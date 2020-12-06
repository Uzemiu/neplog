package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.params.RegisterParam;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.repository.UserRepository;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.service.mapstruct.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User register(RegisterParam param) {
            String bcryptedPassword = BCrypt.hashpw(param.getPassword(),BCrypt.gensalt());
            User user = new User();
            BeanUtils.copyProperties(param,user);
            user.setPassword(bcryptedPassword);
            return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

}
