package cn.neptu.neplog.config;

import cn.neptu.neplog.service.FileService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileServiceFactory implements InitializingBean{

    private final ApplicationContext applicationContext;
    private final Map<String, FileService> fileServiceMap;

    public FileServiceFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.fileServiceMap = new HashMap<>(2);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.fileServiceMap.putAll(applicationContext.getBeansOfType(FileService.class));
    }

    public FileService getFileService(String name){
        FileService fileService = fileServiceMap.get(name);
        Assert.notNull(fileService,"Unknown FileService: " + name);
        return fileService;
    }

}
