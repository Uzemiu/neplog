package cn.neptu.neplog.interceptor;

import cn.neptu.neplog.exception.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {


//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HandlerMethod method = (HandlerMethod)handler;
//        if(!method.hasMethodAnnotation(AuthRequired.class))
//            return true;
//        Assert.notNull(request.getCookies(),"Invalid token.");
//        String token = Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals("AccessToken"))
//                .findAny()
//                .orElseThrow(() -> new BadRequestException("Auth failed Empty Token"))
//                .getValue();
//
//        String uuid = authorizationService.verifyToken(token)
//                .orElseThrow(() -> new BadRequestException("Auth failed Invalid token"));
//
//        authorizationService.tryRefresh(token,uuid).ifPresent(e -> {
//            Cookie cookie = new Cookie("AccessToken",e);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//        });
//
//        return true;
//    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod)handler;
        }

        return true;
    }
}
