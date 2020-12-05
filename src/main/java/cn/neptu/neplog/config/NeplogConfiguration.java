package cn.neptu.neplog.config;

import cn.neptu.neplog.filter.RewriteFilter;
import cn.neptu.neplog.interceptor.AuthenticationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@Configuration
public class NeplogConfiguration implements WebMvcConfigurer {

    @Resource
    private AuthenticationInterceptor authorizationInterceptor;

    @Value("${neplog.file.root:uploads}")
    private String localFileRootPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File path = new File(localFileRootPath);
        String realPath = "file:" + path.getAbsolutePath() + File.separator;
        log.info("Adding '{}' to mapped resource location",realPath);
        registry.addResourceHandler("/uploads/**").addResourceLocations(realPath);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","PUT","DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

//    @Bean
//    public FilterRegistrationBean<RewriteFilter> rewriteFilter(){
//        FilterRegistrationBean<RewriteFilter> frb = new FilterRegistrationBean<>();
//        frb.setFilter(new RewriteFilter());
//        frb.addUrlPatterns("/x/*");
//        frb.setOrder(1);
//        return frb;
//    }

    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("neplog-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

}
