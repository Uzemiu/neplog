package cn.neptu.neplog.interceptor;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.annotation.LevelRequiredAccess;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod)handler;
            if(method.hasMethodAnnotation(AnonymousAccess.class)){
                return true;
            }
            User user = SecurityUtil.getCurrentUser();
            if(user == null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            LevelRequiredAccess minLevel = method.getMethodAnnotation(LevelRequiredAccess.class);
            if(user.getLevel() < (minLevel == null ? 6 : minLevel.value())){
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityUtil.removeCurrentUser();
    }
}
