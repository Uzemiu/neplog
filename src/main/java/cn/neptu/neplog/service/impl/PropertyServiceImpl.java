package cn.neptu.neplog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.neptu.neplog.model.property.TencentCosProperty;
import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.property.BlogProperty;
import cn.neptu.neplog.repository.PropertyRepository;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

import static cn.neptu.neplog.constant.BlogPropertyConstant.*;
import static cn.neptu.neplog.constant.CosPropertyConstant.*;

@Slf4j
@Service("propertyService")
public class PropertyServiceImpl extends AbstractCrudService<Property,Integer> implements PropertyService {

    private final PropertyRepository propertyRepository;

    private final List<String> excludedBlogProperties;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        super(propertyRepository);
        this.propertyRepository = propertyRepository;

        this.excludedBlogProperties = Arrays.asList(
                VALUE_AUTO, INSTALLED, INSTALL_STATUS);
    }

    @Override
    public void resetProperty() {
        User user = SecurityUtil.getCurrentUser();
        Map<String, String> properties = new HashMap<>();
        properties.put(BLOG_NAME, "Neplog");
        properties.put(HOME_PAGE_ARTICLE, "updateTime,desc");
        properties.put(HOME_PAGE_COVER, "3");
        properties.put(FRIEND_PAGE_COVER, "");
        properties.put(DEFAULT_FILE_SERVICE, FILE_SERVICE_LOCAL);
        if(user != null){
            properties.put(BLOG_AVATAR, user.getAvatar());
            properties.put(AUTHOR_NAME, user.getNickname());
        }
        save(properties);
    }

    @Override
    public boolean isInstalled() {
        Optional<Property> config = propertyRepository.findByKey(INSTALL_STATUS);
        return config.map(blogProperty -> blogProperty.getValue().equals(INSTALLED)).orElse(false);
    }

    @Override
    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> save(Map<String, String> properties) {
        properties.forEach((k,v) -> {
            Assert.notNull(v,k + ": must not be null");
        });
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
    public String getDefaultFileService() {
        String fs = propertyRepository.findValueByKey(DEFAULT_FILE_SERVICE);
        return StringUtils.hasText(fs) ? fs : FILE_SERVICE_LOCAL;
    }

    @Override
    public Map<String, String> listPropertiesNotIn(Collection<String> keys) {
        return asMap(propertyRepository.findByKeyNotIn(excludedBlogProperties));
    }

    @Override
    public Property getNotNullByKey(String key) {
        return propertyRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Properties: " + key + " not fount"));
    }

    @Override
    public Optional<Property> getByKey(String key) {
        return propertyRepository.findByKey(key);
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

    @Override
    public Map<String, Object> getCosProperty() {
        Map<String,Object> map = new HashMap<>(1);
        map.put("tencent", getTencentCosProperty().asMap());
        return map;
    }

    @Override
    public TencentCosProperty getTencentCosProperty() {
        Map<String, String> properties = asMap(propertyRepository.findAll());
        return new TencentCosProperty().fromMap(properties);
    }

    @Override
    public BlogProperty getBlogProperty() {
        Map<String, String> properties = asMap(propertyRepository.findAll());
        return BeanUtil.mapToBean(properties, BlogProperty.class, true);
    }

    private Map<String, String> asMap(Collection<Property> properties){
        Map<String, String> result = new HashMap<>(properties.size());
        properties.forEach(property -> result.put(property.getKey(), property.getValue()));
        return result;
    }
}
