package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/property")
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public BaseResponse<?> getProperty(Set<String> keys){
        return BaseResponse.ok("ok", propertyService.listByKeyIn(keys));
    }

    @PostMapping
    public BaseResponse<?> createProperty(@RequestBody Map<String, String> properties){
        propertyService.save(properties);
        return BaseResponse.ok();
    }

    @PutMapping
    public BaseResponse<?> updateProperty(@RequestBody Map<String, String> properties){
        propertyService.save(properties);
        return BaseResponse.ok();
    }

    @DeleteMapping
    public BaseResponse<?> deleteProperty(@RequestBody Set<String> keys){
        propertyService.deleteByKeyIn(keys);
        return BaseResponse.ok();
    }

    @GetMapping("/about")
    @AnonymousAccess
    public BaseResponse<?> getAboutProperty(){
        List<Property> properties = propertyService
                .listByKeyLike("about_%")
                .stream()
                .filter(property -> StringUtils.hasText(property.getValue()))
                .collect(Collectors.toList());
        return BaseResponse.ok("ok", propertyService.asMap(properties));
    }

}
