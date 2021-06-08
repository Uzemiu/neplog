package cn.neptu.neplog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.neptu.neplog.model.config.BlogConfig;
import cn.neptu.neplog.repository.BlogConfigRepository;
import cn.neptu.neplog.service.BlogConfigService;
import cn.neptu.neplog.service.base.AbstractConfigService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("blogConfigService")
public class BlogConfigServiceImpl
        extends AbstractConfigService<BlogConfig, Long>
        implements BlogConfigService {

    private final BlogConfigRepository repository;

    protected BlogConfigServiceImpl(BlogConfigRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public void resetConfig() {
        List<BlogConfig> blogConfigList = repository.findAll();
        BlogConfig config = blogConfigList.isEmpty() ? new BlogConfig() : blogConfigList.get(0);
        config.setBlogName("Neplog");
        config.setGlobalCss("");
        config.setIcp("");
        config.setDefaultFileService("local");
        repository.save(config);
    }

    @Override
    public String getDefaultFileService() {
        return getConfig().getDefaultFileService();
    }

    @Override
    public void updateAvailableFileService(String name, boolean valid) {
        BlogConfig config = getConfig();
        Set<String> services = new HashSet<>(Arrays.asList(config.getAvailableFileService().split(";")));
        if(valid){
            services.add(name);
        } else {
            services.remove(name);
        }
        config.setAvailableFileService(CollectionUtil.join(services, ";"));
        repository.save(config);
    }

    @Override
    public void increaseVisit(String id, Long increment) {
        repository.updateViews(increment);
    }
}
