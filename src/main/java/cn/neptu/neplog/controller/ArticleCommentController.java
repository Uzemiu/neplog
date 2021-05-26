package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.constant.CommentConstant;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.query.ArticleCommentQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.model.support.UserAgentInfo;
import cn.neptu.neplog.service.ArticleCommentService;
import cn.neptu.neplog.utils.RequestUtil;
import cn.neptu.neplog.utils.SecurityUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @GetMapping
    @AnonymousAccess
    public BaseResponse<?> listByArticleId(ArticleCommentQuery query,
                                           @PageableDefault(
                                                   sort = "createTime",
                                                   direction = Sort.Direction.DESC,
                                                   size = 20) Pageable pageable){
        query.setStatus(CommentConstant.STATUS_PUBLIC);
        query.setParentId(0L); // 查询顶层评论
        PageDTO<CommentDTO> all = articleCommentService.queryBy(query, pageable);
        articleCommentService.fillChildren(all.getContent(), query.getArticleId());
        articleCommentService.setUserInfo(all.getContent());
        // 没flatMap
        return BaseResponse.ok("ok", all);
    }

    @GetMapping("/query")
    public BaseResponse<PageDTO<CommentDTO>> queryBy(ArticleCommentQuery query,
                                   @PageableDefault(sort = "createTime",
                                           direction = Sort.Direction.DESC) Pageable pageable){
        return BaseResponse.ok("ok", articleCommentService.queryByWithArticle(query, pageable));
    }

    @PostMapping
    @AnonymousAccess
    public BaseResponse<?> postComment(@RequestBody @Validated CommentDTO commentDTO, HttpServletRequest request){
        String ip = RequestUtil.getIp(request);
        commentDTO.setIpAddress(ip);

        if(!StringUtils.hasText(commentDTO.getUserAgent())
            && !StringUtils.hasText(commentDTO.getOperatingSystem())){
            UserAgent ua = RequestUtil.getUserAgentInfo(request);
            commentDTO.setOperatingSystem(ua.getOperatingSystem().getName());
            commentDTO.setUserAgent(ua.getBrowser().getBrowserType().getName());
        }

        articleCommentService.create(commentDTO);
        return BaseResponse.ok();
    }

    @DeleteMapping
    public BaseResponse<?> deleteComment(@RequestBody List<Long> id){
        articleCommentService.deleteByIdIn(id);
        return BaseResponse.ok("删除评论成功");
    }
}
