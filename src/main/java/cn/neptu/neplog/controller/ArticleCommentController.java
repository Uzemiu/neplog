package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @GetMapping
    @AnonymousAccess
    public BaseResponse<?> listByArticleId(Long articleId){
        List<CommentDTO> all = articleCommentService.listByArticleId(articleId);
        return BaseResponse.ok("ok",articleCommentService.buildSimpleCommentTree(all));
    }

    @PostMapping
    @AnonymousAccess
    public BaseResponse<?> postComment(@RequestBody @Validated CommentDTO commentDTO){
        articleCommentService.create(commentDTO);
        return BaseResponse.ok();
    }
}
