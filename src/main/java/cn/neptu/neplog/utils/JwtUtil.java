package cn.neptu.neplog.utils;

import cn.neptu.neplog.config.security.SecurityConfig;
import cn.neptu.neplog.model.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class JwtUtil implements InitializingBean {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";

    @Resource
    private SecurityConfig securityConfig;
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] key = securityConfig.getSecret().getBytes(UTF_8);
        jwtParser = Jwts.parser()
                .setSigningKey(key);
        jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, key);
    }

    public String generateToken(User user) {
        Date expire = new Date(System.currentTimeMillis() + securityConfig.getTokenExpireTime() * 3600_000);
        String jwt = jwtBuilder
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getId())
                .setExpiration(expire)
                .compact();
        return jwt;
    }

    public Claims parseToken(String token) {
        return jwtParser
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

}
