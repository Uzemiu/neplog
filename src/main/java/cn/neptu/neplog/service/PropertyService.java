package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.service.base.CrudService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PropertyService extends CrudService<Property, Long> {

    Optional<Property> getByKey(String key);

    Optional<String> getValueByKey(String key);

    Property save(String key, String value);

    Property save(Property property);

    List<Property> save(Map<String,String> properties);

    Property getNotNullByKey(String key);

    Map<String, String> listByKeyIn(Collection<String> keys);

    long deleteByKeyIn(Collection<String> keys);

    Map<String, String> asMap(Collection<Property> properties);
}
