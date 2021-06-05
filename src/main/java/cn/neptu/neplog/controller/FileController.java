package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.LevelRequiredAccess;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.entity.Storage;
import cn.neptu.neplog.model.query.StorageQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;

    @ApiOperation("登录用户上传头像")
    @PostMapping("/avatar")
    @LevelRequiredAccess(1)
    public BaseResponse<?> uploadAvatar(@RequestBody MultipartFile file){
        return BaseResponse.ok("ok",
                storageService.upload(file,"avatar"));
    }

    @ApiOperation("获取存储文件信息")
    @GetMapping("/{location}")
    public BaseResponse<PageDTO<Storage>> listFiles(StorageQuery query,
                                                    @PageableDefault(sort = {"updateTime"},size = 20,
                                             direction = Sort.Direction.DESC) Pageable pageable){
        return BaseResponse.ok("ok",
                storageService.queryBy(query,pageable));
    }

    @ApiOperation("上传文件")
    @PostMapping("/{location}/{type}")
    public BaseResponse<String> uploadFile(@RequestBody MultipartFile file,
                                      @PathVariable String location,
                                      @PathVariable String type){
        return BaseResponse.ok("ok",
                storageService.upload(file, type, location).getVirtualPath());
    }

    @ApiOperation("删除文件")
    @DeleteMapping
    public BaseResponse<?> delete(@RequestBody List<Long> id){
        storageService.deleteByIdIn(id);
        return BaseResponse.ok();
    }
}
