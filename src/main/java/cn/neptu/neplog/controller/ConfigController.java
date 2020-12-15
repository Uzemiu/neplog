package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.params.InstallParam;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.repository.BaseRepository;
import cn.neptu.neplog.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @AnonymousAccess
    @GetMapping("/installed")
    public boolean isInstalled(){
        return configService.installed();
    }

    @AnonymousAccess
    @PostMapping("/install")
    public BaseResponse<?> install(@RequestBody @Validated InstallParam installParam){
        configService.installBlog(installParam);
        return BaseResponse.ok("博客已安装成功");
    }

    @AnonymousAccess
    @GetMapping("/blog")
    public BaseResponse<?> blogConfig(){
        return BaseResponse.ok("ok",configService.getBlogConfig());
    }
}
