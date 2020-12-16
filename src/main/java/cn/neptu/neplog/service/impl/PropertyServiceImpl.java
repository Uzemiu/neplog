package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.repository.PropertyRepository;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static cn.neptu.neplog.config.common.BlogConfigConstant.*;

@Slf4j
@Service("configService")
public class PropertyServiceImpl extends AbstractCrudService<Property,Integer> implements PropertyService {

    private final PropertyRepository propertyRepository;

    private final List<String> blogPropertyNames;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        super(propertyRepository);
        this.propertyRepository = propertyRepository;

        this.blogPropertyNames = Arrays.asList(BLOG_NAME, INSTALL_STATUS);
    }

    @Override
    public boolean isInstalled() {
        Optional<Property> config = propertyRepository.getByKey(INSTALL_STATUS);
        return config.map(blogProperty -> blogProperty.getValue().equals(INSTALLED)).orElse(false);
    }

    @Override
    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> save(Map<String, String> properties) {
        List<Property> result = new LinkedList<>();
        properties.forEach((key,value) -> result.add(new Property(null,key,value)));
        return propertyRepository.saveAll(result);
    }

    @Override
    public Map<String, String> getBlogProperty() {
        return listPropertiesIn(blogPropertyNames);
    }

    @Override
    public Map<String, String> listPropertiesIn(Collection<String> keys) {
        List<Property> properties = propertyRepository.getByKeyIn(blogPropertyNames);
        Map<String, String> result = new HashMap<>(keys.size());
        properties.forEach(property -> result.put(property.getKey(), property.getValue()));
        return result;
    }

    @Override
    public Property getNotNullByKey(String key) {
        return propertyRepository.getByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Properties: " + key + " not fount"));
    }

    @Override
    public Optional<Property> getByKey(String key) {
        return propertyRepository.getByKey(key);
    }

    @Override
    public Property save(String key, String value) {
        return propertyRepository.save(new Property(null,key,value));
    }

    @Override
    public void increaseVisit(String integer, Integer increment) {
        Property property = getNotNullByKey(VISIT_COUNT);
        long visit = Long.parseLong(property.getValue());
        visit += increment;
        property.setValue(String.valueOf(visit));
        save(property);
    }
}
