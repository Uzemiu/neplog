package cn.neptu.neplog.filter;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.utils.JwtUtil;
import cn.neptu.neplog.utils.RedisUtil;
import cn.neptu.neplog.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(httpServletRequest);
        if(token != null){
            try{
                Claims claims = jwtUtil.parseToken(token);
                String userId = claims.getSubject();
                Optional<User> user = userService.findById(userId);
                if(!user.isPresent()){
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"User not found");
                    return;
                }
                SecurityUtil.setCurrentUser(user.get());
                Date expire = claims.getExpiration();
            } catch (ExpiredJwtException e){
                log.info("Received expired jwt token {}",token);
            }
            // todo token续期
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
