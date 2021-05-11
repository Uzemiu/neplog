package cn.neptu.neplog.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.factory.ConfigFactory;
import cn.neptu.neplog.event.BlogVisitEvent;
import cn.neptu.neplog.model.config.BlogConfig;
import cn.neptu.neplog.model.dto.UserDTO;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.model.vo.BlogConfigVO;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigFactory configFactory;
    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    @AnonymousAccess
    @GetMapping("/blog")
    public BaseResponse<?> getBlogConfig(HttpServletRequest request){
        eventPublisher.publishEvent(new BlogVisitEvent(this,"",RequestUtil.getIp(request)));
        BlogConfig blogConfig = (BlogConfig) configFactory.getConfigService("blog").getConfig();
        BlogConfigVO vo = new BlogConfigVO();
        BeanUtil.copyProperties(blogConfig, vo, true);

        UserDTO owner = userService.getOwner();
        vo.setAuthorName(owner.getNickname());
        vo.setBlogAvatar(owner.getAvatar());

        return BaseResponse.ok("ok", vo);
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
