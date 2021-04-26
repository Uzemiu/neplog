package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.config.BlogConfig;
import cn.neptu.neplog.repository.BlogConfigRepository;
import cn.neptu.neplog.service.BlogConfigService;
import cn.neptu.neplog.service.base.AbstractConfigService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

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
    public String getDefaultFileService() {
        return repository.findAll().get(0).getFileService();
    }

    @Override
    public void increaseVisit(String id, Long increment) {
        repository.updateViews(increment);
    }
}
