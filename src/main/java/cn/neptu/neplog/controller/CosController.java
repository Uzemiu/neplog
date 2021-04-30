package cn.neptu.neplog.controller;

import cn.neptu.neplog.factory.FileServiceFactory;
import cn.neptu.neplog.model.support.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cos")
@RequiredArgsConstructor
public class CosController {

    private final FileServiceFactory fileServiceFactory;

    @GetMapping
    public BaseResponse<Map<String, Object>> getCosProperties(){
        return BaseResponse.ok("ok");
    }

    @GetMapping("/{cos}/buckets")
    public BaseResponse<?> listBuckets(@PathVariable String cos,
                                       @RequestBody Map<String, String> properties){
        return BaseResponse.ok("ok",fileServiceFactory.getAsCosService(cos).listBuckets(properties));
    }

    @PutMapping("/{cos}")
    public BaseResponse<?> update(@PathVariable String cos,
                                  @RequestBody Map<String, String> properties){
//        CosService cosService = fileServiceFactory.getAsCosService(cos);
//        boolean isValid = cosService.isValid(properties);
//        configService.updateAvailableFileService(cos, isValid);
//        if(isValid){
//            configService.save(properties);
//        } else {
//            throw new BadRequestException("无效的地区或桶名称");
//        }
        return BaseResponse.ok("ok");
    }
}
