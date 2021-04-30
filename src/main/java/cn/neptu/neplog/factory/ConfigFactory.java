package cn.neptu.neplog.factory;

import cn.neptu.neplog.service.ConfigService;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.utils.StringUtil;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ConfigFactory implements InitializingBean {

    private final Map<String, ConfigService> configMap;
    private final ApplicationContext applicationContext;

    public ConfigFactory(ApplicationContext applicationContext) {
        this.configMap = new HashMap<>();
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ConfigService> configServices =
                applicationContext.getBeansOfType(ConfigService.class);
        configServices.putAll(configServices);
        configMap.putAll(configServices);
        // 增加FileService名字缩写的映射
        configServices.forEach((name, service) -> {
            int i = StringUtil.firstCamel(name);
            configMap.put(i > 0 ? name.substring(0,i) : name, service);
        });
    }

    public ConfigService<?, ?> getConfigService(String name){
        ConfigService<?, ?> configService = configMap.get(name);
        Assert.notNull(configService, name + " not found");
        return configService;
    }
}
