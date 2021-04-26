package cn.neptu.neplog.service.base;

import cn.hutool.core.bean.BeanUtil;
import cn.neptu.neplog.exception.InternalException;
import cn.neptu.neplog.model.config.BlogConfig;
import cn.neptu.neplog.model.config.TencentCosConfig;
import cn.neptu.neplog.repository.BaseRepository;
import cn.neptu.neplog.service.ConfigService;

import java.util.List;
import java.util.Map;

public abstract class AbstractConfigService<CONFIG, ID>
        extends AbstractCrudService<CONFIG, ID>
        implements ConfigService<CONFIG, ID> {

    private final BaseRepository<CONFIG, ID> repository;

    protected AbstractConfigService(BaseRepository<CONFIG, ID> repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public void resetConfig() {
        repository.deleteAll();
        createConfig();
    }

    @Override
    public void updateConfig(Map<String, String> configMap) {
        CONFIG old = getConfig();
        BeanUtil.fillBeanWithMap(configMap, old, true);
        repository.save(old);
    }

    @Override
    public CONFIG getConfig() {
        List<CONFIG> config = repository.findAll();
        return config.isEmpty() ? createConfig() : config.get(0);
    }

    private CONFIG createConfig(){
        try {
            CONFIG config = entityClass.newInstance();
            repository.save(config);
            return config;
        } catch (Exception e) {
           throw new InternalException("Failed to create config");
        }
    }
}
