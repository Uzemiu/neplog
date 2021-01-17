package cn.neptu.neplog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.neptu.neplog.constant.LevelConstant;
import cn.neptu.neplog.exception.InternalException;
import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.dto.UserDTO;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.property.BlogProperty;
import cn.neptu.neplog.model.property.MailProperty;
import cn.neptu.neplog.model.property.TencentCosProperty;
import cn.neptu.neplog.repository.PropertyRepository;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

import static cn.neptu.neplog.constant.BlogPropertyConstant.*;
import static cn.neptu.neplog.constant.CosPropertyConstant.AVAILABLE_COS_SERVICE;

@Slf4j
@Service("propertyService")
public class PropertyServiceImpl extends AbstractCrudService<Property,Integer> implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserService userService;

    public PropertyServiceImpl(PropertyRepository propertyRepository, UserService userService) {
        super(propertyRepository);
        this.propertyRepository = propertyRepository;
        this.userService = userService;
    }

    @Override
    public Optional<String> getValueByKey(String key) {
        return propertyRepository.findValueByKey(key);
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
    public Property save(Property property) {
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
    public String getDefaultFileService() {
        return propertyRepository.findValueByKey(DEFAULT_FILE_SERVICE).orElse(FILE_SERVICE_LOCAL);
    }

    @Override
    public Map<String, String> listPropertiesNotIn(Collection<String> keys) {
        return asMap(propertyRepository.findByKeyNotIn(keys));
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
    public Map<String, String> listAsMap() {
        return asMap(propertyRepository.findAll());
    }

    @Override
    public Map<String, String> asMap(Collection<Property> properties){
        Map<String, String> result = new HashMap<>(properties.size());
        properties.forEach(property -> result.put(property.getKey(), property.getValue()));
        return result;
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
    public boolean isInstalled() {
        Optional<Property> config = propertyRepository.findByKey(INSTALL_STATUS);
        return config.map(blogProperty -> blogProperty.getValue().equals(INSTALLED)).orElse(false);
    }

    @Override
    public Set<String> updateAvailableFileService(String cos, boolean availability) {
        Optional<String> value = propertyRepository.findValueByKey(AVAILABLE_COS_SERVICE);
        Set<String> available = value.map(s -> new HashSet<>(Arrays.asList(s.split(";")))).orElseGet(HashSet::new);
        if(availability){
            available.add(cos);
        } else {
            available.remove(cos);
        }
        save(AVAILABLE_COS_SERVICE,CollectionUtil.join(available, ";"));
        return available;
    }

    @Override
    public Map<String, Object> getCosProperties() {
        Map<String,Object> map = new HashMap<>(1);
        map.put("tencent", getTencentCosProperty().asMap());
        return map;
    }

    @Override
    public TencentCosProperty getTencentCosProperty() {
        return new TencentCosProperty(listAsMap());
    }

    @Override
    public MailProperty getMailProperty() {
        return new MailProperty(listAsMap());
    }

    @Override
    public BlogProperty getBlogProperty() {
        BlogProperty blogProperty = new BlogProperty(listAsMap());

        UserDTO owner = userService.getOwner();
        blogProperty.setAuthorName(owner.getNickname());
        blogProperty.setBlogAvatar(owner.getAvatar());

        return blogProperty;
    }
}
