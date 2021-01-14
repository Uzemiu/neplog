package cn.neptu.neplog.controller;

import cn.neptu.neplog.config.FileServiceFactory;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.service.CosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cos")
@RequiredArgsConstructor
public class CosController {

    private final FileServiceFactory fileServiceFactory;
    private final PropertyService propertyService;

    @GetMapping
    public BaseResponse<Map<String, Object>> getCosProperties(){
        return BaseResponse.ok("ok",propertyService.getCosProperties());
    }

    @GetMapping("/{cos}/buckets")
    public BaseResponse<?> listBuckets(@PathVariable String cos,
                                       @RequestBody Map<String, String> properties){
        return BaseResponse.ok("ok",fileServiceFactory.getAsCosService(cos).listBuckets(properties));
    }

    @PutMapping("/{cos}")
    public BaseResponse<?> update(@PathVariable String cos,
                                  @RequestBody Map<String, String> properties){
        CosService cosService = fileServiceFactory.getAsCosService(cos);
        boolean isValid = cosService.isValid(properties);
        propertyService.updateAvailableFileService(cos, isValid);
        if(isValid){
            propertyService.save(properties);
        } else {
            throw new BadRequestException("无效的地区或桶名称");
        }
        return BaseResponse.ok("ok");
    }
}
