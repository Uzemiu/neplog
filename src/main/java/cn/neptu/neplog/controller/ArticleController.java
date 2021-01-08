package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.event.ArticleViewEvent;
import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/view")
    @AnonymousAccess
    public BaseResponse<ArticleBaseDTO> getArticleView(@Validated @NotNull Long id,
                                                       HttpServletRequest request){
        String ip = RequestUtil.getIp(request);
        eventPublisher.publishEvent(new ArticleViewEvent(this,id.toString(),ip));
        return BaseResponse.ok("ok",articleService.findViewById(id));
    }

    @GetMapping({"","/list"})
    @AnonymousAccess
    public BaseResponse<List<ArticleBaseDTO>> queryBy(@Validated ArticleQuery query,
                                                     @PageableDefault(sort = {"updateTime"},
                                                    direction = Sort.Direction.DESC) Pageable pageable){
        Pageable newPageable = andDefaultPageable(pageable);
        // 普通用户默认查询未被删除文章的可见文章
        query.setDeleted(false);
        query.setStatus(ArticleConstant.STATUS_PUBLISHED);
        return BaseResponse.ok("ok",articleService.queryBy(query,newPageable));
    }

    @GetMapping("/detail")
    public BaseResponse<ArticleDTO> getArticleDetail(@Validated @NotNull Long id){
        return BaseResponse.ok("ok",articleService.findDetailById(id));
    }

    /**
     *
     * @param query
     * @param pageable
     * @return
     */
    @GetMapping("/query")
    public BaseResponse<?> privateQueryBy(ArticleQuery query,
                                          @PageableDefault(sort = {"updateTime"},
                                                  direction = Sort.Direction.DESC) Pageable pageable){
        Pageable newPageable = andDefaultPageable(pageable);
        Map<String, Object> res = new HashMap<>();
        res.put("articles",articleService.queryBy(query,newPageable));
        res.put("count",articleService.countByLabel());
        return BaseResponse.ok("ok",res);
    }

    @PostMapping
    public BaseResponse<?> createArticle(){
        articleService.save(new ArticleDTO());
        return BaseResponse.ok("创建文章成功");
    }

    @PutMapping
    public BaseResponse<?> updateArticle(@RequestBody ArticleDTO article){
        Article a = articleService.save(article);
        return BaseResponse.ok("更新文章成功",a.getId());
    }

    @PutMapping("/delete")
    public BaseResponse<?> updateDeleted(@RequestBody ArticleDTO articleDTO){
        return BaseResponse.ok("ok",articleService.trash(articleDTO.getId(), articleDTO.getDeleted()));
    }

    private Pageable andDefaultPageable(Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC, "priority");
        Sort newSort = sort.and(pageable.getSort());
        return PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),newSort);
    }
}
