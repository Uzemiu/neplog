package cn.neptu.neplog.service.impl;

import cn.hutool.core.lang.Assert;
import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.repository.PropertyRepository;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("propertyService")
public class PropertyServiceImpl
        extends AbstractCrudService<Property, Long>
        implements PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        super(propertyRepository);
        this.propertyRepository = propertyRepository;
    }

    @Override
    public Optional<Property> getByKey(String key) {
        return propertyRepository.findByKey(key);
    }

    @Override
    public Optional<String> getValueByKey(String key) {
        return propertyRepository.findValueByKey(key);
    }

    @Override
    public List<Property> listByKeyLike(String key) {
        return propertyRepository.findByKeyLike(key);
    }

    @Override
    public Property save(String key, String value) {
        Property property = propertyRepository.findByKey(key).orElse(new Property(null,key,value));
        property.setValue(value);
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> save(Map<String, String> properties) {
        properties.forEach((k,v) -> Assert.notNull(v,k + ": must not be null"));

        Set<Property> previous = propertyRepository.findByKeyIn(properties.keySet());

        previous.forEach(property -> {
            String value = properties.get(property.getKey());
            if(value != null){
                property.setValue(value);
                properties.remove(property.getKey());
            }
        });
        properties.forEach((k,v) -> previous.add(new Property(null,k,v)));

        return propertyRepository.saveAll(previous);
    }

    @Override
    public Property getNotNullByKey(String key) {
        return propertyRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("key: " + key + " could not be found"));
    }

    @Override
    public Map<String, String> listByKeyIn(Collection<String> keys) {
        return asMap(propertyRepository.findByKeyIn(keys));
    }

    @Override
    public long deleteByKeyIn(Collection<String> keys) {
        return propertyRepository.deletePropertiesByKeyNotIn(keys);
    }

    @Override
    public Map<String, String> asMap(Collection<Property> properties) {
        Map<String, String> result = new HashMap<>(properties.size());
        properties.forEach(property -> result.put(property.getKey(), property.getValue()));
        return result;
    }
}
