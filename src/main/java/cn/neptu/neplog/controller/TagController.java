package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.dto.TagDTO;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.mapstruct.TagMapper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @ApiOperation("查询所有标签")
    @GetMapping
    @AnonymousAccess
    public BaseResponse<List<TagDTO>> listAllTags(){
        return BaseResponse.ok("ok",tagService.listAllDTO());
    }

    @ApiOperation("创建标签")
    @PostMapping
    public BaseResponse<?> createTag(@RequestBody Tag tag){
        Tag saved = tagService.create(tag);
        return BaseResponse.ok("创建标签成功", saved);
    }

    @ApiOperation("更新标签")
    @PutMapping
    public BaseResponse<?> updateTag(@RequestBody Tag tag){
        tagService.update(tag);
        return BaseResponse.ok("更新标签成功");
    }

    @ApiOperation("删除标签")
    @DeleteMapping
    public BaseResponse<?> deleteTag(@RequestBody Long id){
        tagService.deleteById(id);
        return BaseResponse.ok("删除标签成功");
    }
}
