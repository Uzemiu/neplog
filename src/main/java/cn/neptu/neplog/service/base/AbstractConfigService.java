package cn.neptu.neplog.service.base;

import cn.hutool.core.bean.BeanUtil;
import cn.neptu.neplog.exception.InternalException;
import cn.neptu.neplog.repository.BaseRepository;
import cn.neptu.neplog.service.ConfigService;

import java.util.HashMap;
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
    public void updateConfig(String key, String value) {
        Map<String, String> hashMap = new HashMap<>(1);
        hashMap.put(key,value);
        updateConfig(hashMap);
    }

    @Override
    public CONFIG getConfig() {
        List<CONFIG> config = repository.findAll();
        return config.isEmpty() ? createConfig() : config.get(0);
    }

    @Override
    public CONFIG createConfig(){
        try {
            CONFIG config = entityClass.newInstance();
            repository.save(config);
            return config;
        } catch (Exception e) {
           throw new InternalException("Failed to create config");
        }
    }
}
