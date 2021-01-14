package cn.neptu.neplog.service;

import cn.neptu.neplog.model.property.TencentCosProperty;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.model.property.BlogProperty;
import cn.neptu.neplog.service.base.CrudService;

import java.util.*;

public interface PropertyService extends CrudService<Property, Integer>, VisitService {

    boolean isInstalled();

    void resetProperty();

    Optional<String> getValueByKey(String key);

    Property save(Property property);

    List<Property> save(Map<String,String> properties);

    Property save(String key, String value);

    Optional<Property> getByKey(String key);

    Property getNotNullByKey(String key);

    Map<String,String> listPropertiesNotIn(Collection<String> keys);



    String getDefaultFileService();

    BlogProperty getBlogProperty();

    TencentCosProperty getTencentCosProperty();

    Map<String, Object> getCosProperties();

    Set<String> updateAvailableFileService(String cos, boolean availability);

}
