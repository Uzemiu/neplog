package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.RequiredLevelAccess;
import cn.neptu.neplog.config.common.UploadFileConfig;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final UploadFileConfig configuration;

    @PostMapping("/avatar")
    @RequiredLevelAccess(1)
    public BaseResponse<?> uploadAvatar(@RequestBody MultipartFile file){
        return BaseResponse.ok("ok",fileService.upload(file,configuration.get("avatar")));
    }

    @PostMapping("/cover")
    public BaseResponse<?> uploadArticleCover(@RequestBody MultipartFile file){
        return BaseResponse.ok("ok",fileService.upload(file,configuration.get("articleCover")));
    }

    @PostMapping("/image")
    public BaseResponse<?> uploadImage(@RequestBody MultipartFile file){
        return BaseResponse.ok("ok",fileService.upload(file,configuration.get("image")));
    }

    @PostMapping
    public BaseResponse<?> uploadFile(@RequestBody MultipartFile file){
        return BaseResponse.ok("ok",fileService.upload(file,configuration.get("file")));
    }

    @DeleteMapping
    public BaseResponse<?> delete(@RequestBody String path){
        fileService.delete(path);
        return BaseResponse.ok();
    }
}
