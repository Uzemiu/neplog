package cn.neptu.neplog.config;

import cn.neptu.neplog.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class FilterConfig {

    @Resource
    private AuthenticationFilter authenticationFilter;

    /**
     * 用户认证过滤器
     */
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(){
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authenticationFilter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("authenticationFilter");
        registration.setOrder(1);
        return registration;
    }
}
