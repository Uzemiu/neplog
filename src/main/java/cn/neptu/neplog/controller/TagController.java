package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.mapstruct.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    @AnonymousAccess
    public BaseResponse<?> listAllTags(){
        return BaseResponse.ok("ok",tagMapper.toDto(tagService.listAll()));
    }
}
