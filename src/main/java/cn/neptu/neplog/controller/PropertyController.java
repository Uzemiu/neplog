package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @AnonymousAccess
    @GetMapping("/blog")
    public BaseResponse<?> blogProperty(){
        return BaseResponse.ok("ok", propertyService.getBlogProperty());
    }

    @GetMapping("/cos")
    public BaseResponse<Map<String,Object>> getCosProperty(){
        return BaseResponse.ok("ok",propertyService.getCosProperty());
    }

    @PostMapping("/reset")
    public BaseResponse<?> resetProperty(){
        propertyService.resetProperty();
        return BaseResponse.ok("重置配置成功");
    }

    @PutMapping
    public BaseResponse<?> updateProperty(@RequestBody Map<String, String> properties){
        propertyService.save(properties);
        return BaseResponse.ok("更新配置成功");
    }
}
