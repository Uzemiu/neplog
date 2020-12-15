package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.params.RegisterParam;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.repository.BaseRepository;
import cn.neptu.neplog.repository.UserRepository;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service("userService")
public class UserServiceImpl extends AbstractCrudService<User, String> implements UserService {

    private final UserRepository userRepository;
    @Resource
    private UserMapper userMapper;

    protected UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User register(RegisterParam param) {
        String bcryptedPassword = BCrypt.hashpw(param.getPassword(),BCrypt.gensalt());
        User user = new User();
        BeanUtils.copyProperties(param,user);
        user.setPassword(bcryptedPassword);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getByLevel(Integer level) {
        return userRepository.getByLevel(level);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

}
