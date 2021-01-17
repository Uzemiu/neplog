package cn.neptu.neplog.service;

import cn.neptu.neplog.model.property.MailProperty;
import cn.neptu.neplog.model.property.TencentCosProperty;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.model.property.BlogProperty;
import cn.neptu.neplog.service.base.CrudService;

import java.util.*;

public interface PropertyService extends CrudService<Property, Integer>, VisitService {

    void resetProperty();

    Optional<String> getValueByKey(String key);

    Property save(String key, String value);

    Property save(Property property);

    List<Property> save(Map<String,String> properties);

    Optional<Property> getByKey(String key);

    Property getNotNullByKey(String key);

    Map<String, String> listPropertiesNotIn(Collection<String> keys);

    Map<String, String> asMap(Collection<Property> properties);

    Map<String, String> listAsMap();

    // 具体属性/配置部分

    boolean isInstalled();

    String getDefaultFileService();

    BlogProperty getBlogProperty();

    MailProperty getMailProperty();

    TencentCosProperty getTencentCosProperty();

    Map<String, Object> getCosProperties();

    Set<String> updateAvailableFileService(String cos, boolean availability);

}
