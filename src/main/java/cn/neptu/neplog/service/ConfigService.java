package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Config;
import cn.neptu.neplog.model.params.InstallParam;
import cn.neptu.neplog.model.support.BlogConfig;
import cn.neptu.neplog.service.base.CrudService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ConfigService extends CrudService<Config, Integer> {

    boolean installed();

    Config save(Config config);

    List<Config> save(Map<String,String> blogConfig);

    Config save(String key, String value);

    Map<String,String> listConfigsIn(Collection<String> keys);

    Map<String, String> getBlogConfig();

    @Transactional
    void installBlog(InstallParam installParam);
}
