package cn.neptu.neplog.config;

import cn.neptu.neplog.model.params.InstallParam;
import cn.neptu.neplog.service.InstallService;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializingConfig {

    private final PropertyService propertyService;
    private final InstallService installService;
    private final AESUtil aesUtil;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(!propertyService.isInstalled()){
            InstallParam installParam = new InstallParam();
            installParam.setBlogName("Neplog");
            installParam.setEmail("neplog@neplog.com");
            installParam.setNickname("neplog");
            installParam.setUsername("neplog");
            installParam.setPassword(aesUtil.encrypt("neplog"));
            installService.installBlog(installParam);
        }
    }
}
