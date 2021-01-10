package cn.neptu.neplog.service;

import cn.neptu.neplog.model.property.TencentCosProperty;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.model.property.BlogProperty;
import cn.neptu.neplog.service.base.CrudService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PropertyService extends CrudService<Property, Integer>, VisitService {

    boolean isInstalled();

    void resetProperty();

    Property save(Property property);

    List<Property> save(Map<String,String> blogConfig);

    Property save(String key, String value);

    Optional<Property> getByKey(String key);

    Property getNotNullByKey(String key);

    Map<String,String> listPropertiesNotIn(Collection<String> keys);

    String getDefaultFileService();

    BlogProperty getBlogProperty();

    TencentCosProperty getTencentCosProperty();

    Map<String, Object> getCosProperty();

}
