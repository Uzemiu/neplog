package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.constant.CommentConstant;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.model.support.UserAgentInfo;
import cn.neptu.neplog.service.ArticleCommentService;
import cn.neptu.neplog.utils.RequestUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
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
    public BaseResponse<?> listByArticleId(Long id){
        List<CommentDTO> all = articleCommentService.listByArticleIdAndStatus(id, CommentConstant.STATUS_PUBLIC);
        // 没flatMap
        return BaseResponse.ok("ok",articleCommentService.buildSimpleCommentTree(all));
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
}
