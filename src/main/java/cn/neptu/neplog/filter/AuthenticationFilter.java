package cn.neptu.neplog.filter;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.utils.JwtUtil;
import cn.neptu.neplog.utils.RedisUtil;
import cn.neptu.neplog.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims = jwtUtil.parseToken(token);

        String userId = claims.getSubject();
        User user = userService.findById(userId).orElseThrow(() -> new BadRequestException("User Not Found"));
        securityUtil.setCurrentUser(user);

        Date expire = claims.getExpiration();

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
