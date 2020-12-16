package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.service.base.CrudService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PropertyService extends CrudService<Property, Integer>, VisitService {

    boolean isInstalled();

    Property save(Property property);

    List<Property> save(Map<String,String> blogConfig);

    Property save(String key, String value);

    Optional<Property> getByKey(String key);

    Property getNotNullByKey(String key);

    Map<String,String> listPropertiesIn(Collection<String> keys);

    Map<String, String> getBlogProperty();

}
