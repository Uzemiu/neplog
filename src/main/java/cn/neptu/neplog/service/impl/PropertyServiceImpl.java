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
@Service("propertyService")
public class PropertyServiceImpl extends AbstractCrudService<Property,Integer> implements PropertyService {

    private final PropertyRepository propertyRepository;

    private final List<String> blogPropertyNames;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        super(propertyRepository);
        this.propertyRepository = propertyRepository;

        this.blogPropertyNames = Arrays.asList(
                BLOG_NAME, INSTALL_STATUS, VISIT_COUNT, INSTALL_TIME,
                HOME_PAGE_ARTICLE, HOME_PAGE_GLIDE, FRIEND_PAGE_COVER);
    }

    @Override
    public void resetProperty() {
        Map<String, String> properties = new HashMap<>();
        properties.put(BLOG_NAME, "Neplog");
        properties.put(HOME_PAGE_ARTICLE, "updateTime,desc");
        properties.put(HOME_PAGE_GLIDE, "auto");
        properties.put(VISIT_COUNT, "0");
        properties.put(FRIEND_PAGE_COVER, "");
        save(properties);
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
        Set<Property> previous = propertyRepository.getByKeyIn(properties.keySet());
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
    public Map<String, String> getBlogProperty() {
        return listPropertiesIn(blogPropertyNames);
    }

    @Override
    public Map<String, String> listPropertiesIn(Collection<String> keys) {
        Set<Property> properties = propertyRepository.getByKeyIn(blogPropertyNames);
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
        Property property = getByKey(key).orElse(new Property(null,key,value));
        property.setValue(value);
        return save(property);
    }

    @Override
    public void increaseVisit(String integer, Long increment) {
        Property property = getNotNullByKey(VISIT_COUNT);
        long visit = Long.parseLong(property.getValue());
        visit += increment;
        property.setValue(String.valueOf(visit));
        save(property);
    }
}
