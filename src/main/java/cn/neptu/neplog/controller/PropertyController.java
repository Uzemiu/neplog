package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.MailService;
import cn.neptu.neplog.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final MailService mailService;

    @AnonymousAccess
    @GetMapping("/blog")
    public BaseResponse<?> getBlogProperty(){
        return BaseResponse.ok("ok", propertyService.getBlogProperty());
    }

    @GetMapping("/mail")
    public BaseResponse<?> getMailProperty(){
        return BaseResponse.ok("ok", propertyService.getMailProperty().asMap());
    }

    @GetMapping
    public BaseResponse<?> getProperty(String key){
        return BaseResponse.ok("ok",propertyService.getValueByKey(key).orElse(""));
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

    @PostMapping("/mail/test")
    public BaseResponse<?> testMailConnection(){
        mailService.testConnection();
        return BaseResponse.ok();
    }
}
