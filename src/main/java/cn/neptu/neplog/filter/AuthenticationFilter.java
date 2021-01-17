package cn.neptu.neplog.filter;

import cn.neptu.neplog.config.security.SecurityConfig;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.utils.TokenUtil;
import cn.neptu.neplog.utils.RedisUtil;
import cn.neptu.neplog.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;
    private final SecurityConfig securityConfig;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = TokenUtil.resolveToken(httpServletRequest);
        if(token != null){
            String userId = (String) redisUtil.get(token);
            if(userId != null){
                Optional<User> user = userService.getById(userId);
                if(!user.isPresent()){
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"User not found");
                    return;
                }
                SecurityUtil.setCurrentUser(user.get());

                long expire = redisUtil.getExpire(token);
                if(expire < securityConfig.getTokenRefreshTime() * 3600_000){
                    redisUtil.expire(token, securityConfig.getTokenExpireTime(), TimeUnit.HOURS);
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
