package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.params.InstallParam;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.InstallService;
import cn.neptu.neplog.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final InstallService installService;
    private final PropertyService propertyService;

    @AnonymousAccess
    @GetMapping("/installed")
    public boolean isInstalled(){
        return propertyService.isInstalled();
    }

    @AnonymousAccess
    @PostMapping("/install")
    public BaseResponse<?> install(@RequestBody @Validated InstallParam installParam){
        installService.installBlog(installParam);
        return BaseResponse.ok("博客已安装成功");
    }

    @AnonymousAccess
    @GetMapping("/blog")
    public BaseResponse<?> blogConfig(){
        return BaseResponse.ok("ok", propertyService.getBlogProperty());
    }
}
