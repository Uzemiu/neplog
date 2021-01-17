package cn.neptu.neplog.utils;

import cn.neptu.neplog.config.security.SecurityConfig;
import cn.neptu.neplog.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenUtil{

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";

    private final RedisUtil redisUtil;
    private final SecurityConfig securityConfig;

    public String generateAndSetToken(User user) {
        String key = System.currentTimeMillis() + user.getUsername();
        String token = UUID.nameUUIDFromBytes(key.getBytes()).toString();
        redisUtil.set(token, user.getId(), securityConfig.getTokenExpireTime(), TimeUnit.HOURS);
        return token;
    }

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

}
