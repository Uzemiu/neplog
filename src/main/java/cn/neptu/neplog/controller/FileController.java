package cn.neptu.neplog.controller;

import cn.neptu.neplog.model.query.StorageQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;

    @PostMapping("/avatar")
//    @LevelRequiredAccess(1)
    public BaseResponse<?> uploadAvatar(@RequestBody MultipartFile file){
        return BaseResponse.ok("ok",
                storageService.upload(file,"avatar"));
    }

    @GetMapping("/{location}")
    public BaseResponse<?> listFiles(@PathVariable String location, StorageQuery query,
                                     @PageableDefault(sort = {"updateTime"},size = 20,
                                             direction = Sort.Direction.DESC) Pageable pageable){
        return BaseResponse.ok("ok",
                storageService.listFiles(query,pageable));
    }

    @PostMapping("/{location}/{type}")
    public BaseResponse<?> uploadFile(@RequestBody MultipartFile file,
                                      @PathVariable String location,
                                      @PathVariable String type){
        return BaseResponse.ok("ok",
                storageService.upload(file, type, location).getVirtualPath());
    }

    @DeleteMapping
    public BaseResponse<?> delete(@RequestBody String path){
        storageService.delete(path);
        return BaseResponse.ok();
    }
}
