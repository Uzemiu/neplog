package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.entity.Property;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.PropertyService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("查询属性")
    @GetMapping
    public BaseResponse<Map<String, String>> getProperty(Set<String> keys){
        return BaseResponse.ok("ok", propertyService.listByKeyIn(keys));
    }

    @ApiOperation("创建属性")
    @PostMapping
    public BaseResponse<?> createProperty(@RequestBody Map<String, String> properties){
        propertyService.save(properties);
        return BaseResponse.ok();
    }

    @ApiOperation("更新属性")
    @PutMapping
    public BaseResponse<?> updateProperty(@RequestBody Map<String, String> properties){
        propertyService.save(properties);
        return BaseResponse.ok();
    }

    @ApiOperation("删除属性")
    @DeleteMapping
    public BaseResponse<?> deleteProperty(@RequestBody Set<String> keys){
        propertyService.deleteByKeyIn(keys);
        return BaseResponse.ok();
    }

    @ApiOperation("查询关于页面属性")
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
