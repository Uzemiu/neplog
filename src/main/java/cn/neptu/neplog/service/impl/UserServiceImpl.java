package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.constant.LevelConstant;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.exception.InternalException;
import cn.neptu.neplog.model.dto.UserDTO;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.params.LoginParam;
import cn.neptu.neplog.model.params.RegisterParam;
import cn.neptu.neplog.model.params.ResetPasswordParam;
import cn.neptu.neplog.model.support.VerificationCode;
import cn.neptu.neplog.repository.UserRepository;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.UserMapper;
import cn.neptu.neplog.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service("userService")
public class UserServiceImpl extends AbstractCrudService<User, String> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VerificationCodeUtil verificationCodeUtil;
    private final AESUtil aesUtil;
    private final TokenUtil tokenUtil;
    private final RedisUtil redisUtil;
    private final Pattern passwordPattern;

    protected UserServiceImpl(UserRepository userRepository,
                              UserMapper userMapper,
                              VerificationCodeUtil verificationCodeUtil,
                              AESUtil aesUtil,
                              TokenUtil tokenUtil,
                              RedisUtil redisUtil) {
        super(userRepository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.verificationCodeUtil = verificationCodeUtil;
        this.aesUtil = aesUtil;
        this.tokenUtil = tokenUtil;
        this.redisUtil = redisUtil;
        this.passwordPattern = Pattern.compile("^[0-9A-Za-z@#$%^&*()_+!]{6,31}$");
    }

    @Override
    public User register(RegisterParam param) {
        verificationCodeUtil.verify(new VerificationCode(param.getCaptcha(),null, param.getUuid()));

        param.setPassword(aesUtil.decrypt(param.getPassword()));
        Assert.isTrue(passwordPattern.matcher(param.getPassword()).matches(),
                "密码长度须在6~31个字符之间且只能包含大小写字母，数字与@#$%^&*()_+!");

        String bcryptedPassword = BCrypt.hashpw(param.getPassword(),BCrypt.gensalt());
        User user = new User();
        BeanUtils.copyProperties(param,user);
        user.setPassword(bcryptedPassword);

        return userRepository.save(user);
    }

    @Override
    public UserDTO getOwner() {
        User owner =  userRepository.findByLevel(LevelConstant.LEVEL_OWNER)
                .orElseThrow(() -> new InternalException("无法获取博主信息"));
        return userMapper.toDto(owner);
    }

    @Override
    public void resetPassword(ResetPasswordParam param) {
        User user = SecurityUtil.getCurrentUser();
        // 校验原密码
        String plainOldPassword = aesUtil.decrypt(param.getOldPassword());
        Assert.isTrue(BCrypt.checkpw(plainOldPassword,user.getPassword()),"原密码错误");

        String plainNewPassword = aesUtil.decrypt(param.getNewPassword());
        Assert.isTrue(passwordPattern.matcher(plainNewPassword).matches(),
                "密码长度须在6~31个字符之间且只能包含大小写字母，数字与@#$%^&*()_+!");

        String bcryptedPassword = BCrypt.hashpw(plainNewPassword,BCrypt.gensalt());
        user.setPassword(bcryptedPassword);

        userRepository.save(user);
    }

    @Override
    public void logout(HttpServletRequest request) {
        redisUtil.del(TokenUtil.resolveToken(request));
    }

    @Override
    public Map<String, Object> login(LoginParam param) {
        if(SecurityUtil.isLogin()){
            throw new BadRequestException("你已经登陆过了");
        }
        // 校验验证码
        verificationCodeUtil.verify(new VerificationCode(param.getCaptcha(),null, param.getUuid()));
        // 校验密码
        String plainPassword = aesUtil.decrypt(param.getPassword());
        User user = getByUsername(param.getUsername())
                .orElseThrow(() -> new BadRequestException("用户名或密码错误"));
        Assert.isTrue(BCrypt.checkpw(plainPassword,user.getPassword()),"用户名或密码错误");

        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("user", userMapper.toDto(user));
            put("token", TokenUtil.TOKEN_PREFIX + tokenUtil.generateAndSetToken(user));
        }};
        return res;
    }

    @Override
    public User update(UserDTO userDTO) {
        User user = SecurityUtil.getCurrentUser();

        user.setUsername(userDTO.getUsername());
        user.setAvatar(userDTO.getAvatar());
        user.setNickname(userDTO.getNickname());
        user.setSite(userDTO.getSite());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void resetBlogPassword() {
        User owner = userRepository.findByLevel(LevelConstant.LEVEL_OWNER).orElse(null);
        if(owner == null){
            return;
        }
        owner.setUsername("neplog");
        String bcryptedPassword = BCrypt.hashpw("neplog",BCrypt.gensalt());
        owner.setPassword(bcryptedPassword);
        userRepository.save(owner);
    }
}
