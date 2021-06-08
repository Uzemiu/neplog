package cn.neptu.neplog.service;

import cn.neptu.neplog.service.base.CrudService;

import java.util.Map;

public interface ConfigService<CONFIG, ID> extends CrudService<CONFIG, ID>{

    void resetConfig();

    CONFIG createConfig();

    void updateConfig(String key, String value);

    void updateConfig(Map<String, String> configMap);

    CONFIG getConfig();

}
