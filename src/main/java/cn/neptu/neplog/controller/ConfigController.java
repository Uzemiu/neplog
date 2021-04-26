package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.config.ConfigFactory;
import cn.neptu.neplog.event.BlogVisitEvent;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.MailService;
import cn.neptu.neplog.service.ConfigService;
import cn.neptu.neplog.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigFactory configFactory;
    private final ApplicationEventPublisher eventPublisher;

    @AnonymousAccess
    @GetMapping("/blog")
    public BaseResponse<?> getBlogConfig(HttpServletRequest request){
        eventPublisher.publishEvent(new BlogVisitEvent(this,"",RequestUtil.getIp(request)));
        return BaseResponse.ok("ok", configFactory.getConfigService("blog").getConfig());
    }

    @GetMapping("/pluto/{config}")
    public BaseResponse<?> getConfig(@PathVariable String config){
        return BaseResponse.ok("ok", configFactory.getConfigService(config).getConfig());
    }

    @PutMapping("/pluto/{config}")
    @PostMapping("/pluto/{config}")
    public BaseResponse<?> updateProperty(@RequestBody Map<String, String> properties,
                                          @PathVariable String config){
        configFactory.getConfigService(config).updateConfig(properties);
        return BaseResponse.ok("更新配置成功");
    }

    @DeleteMapping("/pluto/{config}")
    public BaseResponse<?> resetProperty(@PathVariable String config){
        configFactory.getConfigService(config).resetConfig();
        return BaseResponse.ok("重置配置成功");
    }

}
