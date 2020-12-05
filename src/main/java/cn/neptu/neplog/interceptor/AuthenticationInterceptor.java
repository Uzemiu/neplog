package cn.neptu.neplog.interceptor;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.annotation.RequiredLevel;
import cn.neptu.neplog.exception.UnauthenticatedException;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final SecurityUtil securityUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod)handler;
            if(method.hasMethodAnnotation(AnonymousAccess.class)){
                return true;
            }
            User user = securityUtil.getCurrentUser();
            if(user == null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            RequiredLevel minLevel = method.getMethodAnnotation(RequiredLevel.class);
            if(user.getLevel() < (minLevel == null ? 6 : minLevel.value())){
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        }
        return true;
    }
}
