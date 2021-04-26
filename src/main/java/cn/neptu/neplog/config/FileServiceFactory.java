package cn.neptu.neplog.config;

import cn.neptu.neplog.service.BlogConfigService;
import cn.neptu.neplog.service.CosService;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.service.ConfigService;
import cn.neptu.neplog.utils.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Component
public class FileServiceFactory implements InitializingBean{

    private final ApplicationContext applicationContext;
    private final BlogConfigService blogConfigService;
    private final Map<String, FileService> fileServiceMap;

    public FileServiceFactory(ApplicationContext applicationContext,
                              BlogConfigService blogConfigService) {
        this.applicationContext = applicationContext;
        this.blogConfigService = blogConfigService;
        this.fileServiceMap = new HashMap<>(3);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, FileService> fileServices = applicationContext.getBeansOfType(FileService.class);
        fileServiceMap.putAll(fileServices);
        // 增加FileService名字缩写的映射
        fileServices.forEach((name, service) -> {
            int i = StringUtil.firstCamel(name);
            fileServiceMap.put(i > 0 ? name.substring(0,i) : name, service);
        });
    }

    public CosService getAsCosService(String name){
        FileService fileService = getFileService(name);
        Assert.isTrue(fileService instanceof CosService, name + " is not the instance of CosService");
        return (CosService) fileService;
    }

    public FileService getFileService(){
        return getFileService("default");
    }

    public FileService getFileService(String name){
        if(name.equalsIgnoreCase("default")){
            name = blogConfigService.getDefaultFileService();
        }
        FileService fileService = fileServiceMap.getOrDefault(name, fileServiceMap.get("local"));
        Assert.notNull(fileService,"Unknown FileService: " + name);
        return fileService;
    }

}
