package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.entity.ArticleLike;
import cn.neptu.neplog.model.entity.BaseLike;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.impl.ArticleLikeServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/like/article")
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeServiceImpl articleLikeService;

    @ApiOperation("获取文章点赞情况")
    @GetMapping
    @AnonymousAccess
    public BaseResponse<ArticleLike> getLike(Long id, HttpServletRequest request){
        return BaseResponse.ok("ok", articleLikeService.getLikeByTargetIdAndIdentity(id, request));
    }

    @ApiOperation("(取消)点赞文章")
    @PostMapping
    @AnonymousAccess
    public BaseResponse<ArticleLike> postLike(@RequestBody ArticleLike like,
                                    HttpServletRequest request){
        articleLikeService.updateLike(like, request);
        return BaseResponse.ok("ok", like);
    }
}
