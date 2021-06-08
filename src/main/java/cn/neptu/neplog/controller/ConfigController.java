package cn.neptu.neplog.controller;

import cn.hutool.core.date.DateUtil;
import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.factory.ConfigFactory;
import cn.neptu.neplog.event.BlogVisitEvent;
import cn.neptu.neplog.model.config.BlogConfig;
import cn.neptu.neplog.model.dto.UserDTO;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.utils.RequestUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final PropertyService propertyService;
    private final ConfigFactory configFactory;
    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    @ApiOperation("获取博客配置")
    @GetMapping("/blog")
    @AnonymousAccess
    public BaseResponse<?> getBlogConfig(HttpServletRequest request){
        BlogConfig blogConfig = (BlogConfig) configFactory.getConfigService("blog").getConfig();
        UserDTO owner = userService.getOwner();

        Map<String, String> res = new HashMap<>();
        res.put("blogName", blogConfig.getBlogName());
        res.put("visitCount", blogConfig.getVisitCount().toString());
        res.put("installTime", DateUtil.format(blogConfig.getInstallTime(), "yyyy-MM-dd"));
        res.put("icp", blogConfig.getIcp());
        res.put("globalCss", blogConfig.getGlobalCss());
        res.put("blogAvatar", owner.getAvatar());
        res.put("authorName", owner.getNickname());

        res.putAll(propertyService.asMap(propertyService.listByKeyLike("glide_image_%")));
        res.putAll(propertyService.asMap(propertyService.listByKeyLike("glide_title_%")));

        eventPublisher.publishEvent(new BlogVisitEvent(this,"0",RequestUtil.getIp(request)));

        return BaseResponse.ok("ok", res);
    }

    @ApiOperation("获取配置信息")
    @GetMapping("/pluto/{config}")
    public BaseResponse<?> getConfig(@PathVariable String config){
        return BaseResponse.ok("ok", configFactory.getConfigService(config).getConfig());
    }

    @ApiOperation("创建配置信息")
    @PostMapping("/pluto/{config}")
    public BaseResponse<?> createProperty(@RequestBody Map<String, String> properties,
                                          @PathVariable String config){
        configFactory.getConfigService(config).updateConfig(properties);
        return BaseResponse.ok("创建配置成功");
    }

    @ApiOperation("更新配置信息")
    @PutMapping("/pluto/{config}")
    public BaseResponse<?> updateProperty(@RequestBody Map<String, String> properties,
                                          @PathVariable String config){
        configFactory.getConfigService(config).updateConfig(properties);
        return BaseResponse.ok("更新配置成功");
    }

    @ApiOperation("重置配置信息")
    @DeleteMapping("/pluto/{config}")
    public BaseResponse<?> resetProperty(@PathVariable String config){
        configFactory.getConfigService(config).resetConfig();
        return BaseResponse.ok("重置配置成功");
    }

}
