package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.entity.ArticleLike;
import cn.neptu.neplog.model.entity.BaseLike;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.impl.ArticleLikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/like/article")
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeServiceImpl articleLikeService;

    @GetMapping
    @AnonymousAccess
    public BaseResponse<?> getLike(Long id, HttpServletRequest request){
        return BaseResponse.ok("ok", articleLikeService.getLikeByTargetIdAndIdentity(id, request));
    }

    @PostMapping
    @AnonymousAccess
    public BaseResponse<?> postLike(@RequestBody ArticleLike like,
                                    HttpServletRequest request){
        articleLikeService.updateLike(like, request);
        return BaseResponse.ok("ok", like);
    }
}
