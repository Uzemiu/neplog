package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.event.ArticleViewEvent;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.params.query.ArticleQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.utils.SecurityUtil;
import cn.neptu.neplog.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.IPv6Utils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/detail")
    public BaseResponse<ArticleDTO> getArticleDetail(@Validated @NotNull Integer id){
        return BaseResponse.ok("ok",articleService.findDetailById(id));
    }

    @GetMapping("/view")
    @AnonymousAccess
    public BaseResponse<ArticleBaseDTO> getArticleView(@Validated @NotNull Integer id,
                                                       HttpServletRequest request){
        String ip = StringUtil.getIp(request);
        eventPublisher.publishEvent(new ArticleViewEvent(this,id.toString(),ip));
        return BaseResponse.ok("ok",articleService.findViewById(id));
    }

    @GetMapping
    @AnonymousAccess
    public BaseResponse<List<ArticleBaseDTO>> queryBy(@Validated ArticleQuery query,
                                                     @PageableDefault(sort = {"updateTime"},
                                                    direction = Sort.Direction.DESC) Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC, "priority");
        sort.and(pageable.getSort());
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),sort);
        // 普通用户默认查询未被删除文章的可见文章
        if(query.getDeleted() == null){
            query.setDeleted(false);
        }
        if(query.getStatus() == null){
            query.setStatus(ArticleConstant.STATUS_PUBLISHED);
        }
        return BaseResponse.ok("ok",articleService.queryBy(query,newPageable));
    }

    @PostMapping
    public BaseResponse<?> createArticle(){
        articleService.save(new ArticleDTO());
        return BaseResponse.ok("创建文章成功");
    }

    @PutMapping
    public BaseResponse<?> updateArticle(@RequestBody ArticleDTO article){
        articleService.save(article);
        return BaseResponse.ok("更新文章成功");
    }

    @PutMapping("/delete")
    public BaseResponse<?> updateDeleted(@RequestBody ArticleDTO articleDTO){
        return BaseResponse.ok("ok",articleService.trash(articleDTO.getId(), articleDTO.getDeleted()));
    }
}
