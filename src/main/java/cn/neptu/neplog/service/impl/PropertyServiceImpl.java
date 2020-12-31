package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.repository.PropertyRepository;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static cn.neptu.neplog.constant.BlogPropertyConstant.*;

@Slf4j
@Service("propertyService")
public class PropertyServiceImpl extends AbstractCrudService<Property,Integer> implements PropertyService {

    private final PropertyRepository propertyRepository;

    private final List<String> excludedBlogProperties;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        super(propertyRepository);
        this.propertyRepository = propertyRepository;

        this.excludedBlogProperties = Arrays.asList(
                VALUE_AUTO, INSTALLED);
    }

    @Override
    public void resetProperty() {
        User user = SecurityUtil.getCurrentUser();
        Map<String, String> properties = new HashMap<>();
        properties.put(BLOG_NAME, "Neplog");
        properties.put(BLOG_AVATAR, user.getAvatar());
        properties.put(HOME_PAGE_ARTICLE, "updateTime,desc");
        properties.put(HOME_PAGE_COVER, "3");
        properties.put(FRIEND_PAGE_COVER, "");
        properties.put(AUTHOR_NAME, user.getNickname());
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
        return listPropertiesNotIn(excludedBlogProperties);
    }

    @Override
    public Map<String, String> listPropertiesNotIn(Collection<String> keys) {
        Set<Property> properties = propertyRepository.getByKeyNotIn(excludedBlogProperties);
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
        Property property = getByKey(VISIT_COUNT).orElse(new Property(null,VISIT_COUNT,"0"));
        long visit = Long.parseLong(property.getValue());
        visit += increment;
        property.setValue(String.valueOf(visit));
        save(property);
    }
}
