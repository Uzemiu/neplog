package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.LevelRequiredAccess;
import cn.neptu.neplog.config.FileServiceFactory;
import cn.neptu.neplog.config.common.UploadFileConfig;
import cn.neptu.neplog.constant.LevelConstant;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceFactory fileServiceFactory;
    private final PropertyService propertyService;
    private final UploadFileConfig configuration;

    @PostMapping("/avatar")
    @LevelRequiredAccess(1)
    public BaseResponse<?> uploadAvatar(@RequestBody MultipartFile file){
        String fs = propertyService.getDefaultFileService();
        return BaseResponse.ok("ok", fileServiceFactory.getFileService(fs).upload(file,configuration.get("avatar")));
    }

    @PostMapping("/{location}/{type}")
    public BaseResponse<?> uploadFile(@RequestBody MultipartFile file,
                                      @PathVariable String location,
                                      @PathVariable String type){
        if(location.equalsIgnoreCase("default")){
            location = propertyService.getDefaultFileService();
        }
        return BaseResponse.ok("ok", fileServiceFactory.getFileService(location).upload(file,configuration.get(type)));
    }

    @DeleteMapping("/{location}")
    public BaseResponse<?> delete(@RequestBody String path,
                                  @PathVariable String location){
        fileServiceFactory.getFileService(location).delete(path);
        return BaseResponse.ok();
    }
}
