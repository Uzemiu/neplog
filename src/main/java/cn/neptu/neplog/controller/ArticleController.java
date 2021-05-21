package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.event.ArticleViewEvent;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.mapstruct.CategoryMapper;
import cn.neptu.neplog.utils.RequestUtil;
import cn.neptu.neplog.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ApplicationEventPublisher eventPublisher;
    private final CategoryMapper categoryMapper;

    @ApiOperation("普通用户查询文章信息")
    @GetMapping("/view")
    @AnonymousAccess
    public BaseResponse<ArticleDTO> getArticleView(@Validated @NotNull Long id,
                                                   HttpServletRequest request){
        String ip = RequestUtil.getIp(request);
        ArticleDTO articleDTO = articleService.findViewById(id);
        eventPublisher.publishEvent(new ArticleViewEvent(this,id.toString(),ip));
        return BaseResponse.ok("ok", articleDTO);
    }

    @ApiOperation("普通用户文章搜索")
    @GetMapping({"","/query"})
    @AnonymousAccess
    public BaseResponse<PageDTO<ArticleDTO>> queryBy(@Validated ArticleQuery query,
                                                     @PageableDefault(sort = {"updateTime"},
                                                    direction = Sort.Direction.DESC) Pageable pageable){
        Pageable newPageable = andDefaultPageable(pageable);
        if(!SecurityUtil.isOwner()){
            // 普通用户默认查询未被删除文章的可见文章
            query.setDeleted(false);
            query.setStatus(ArticleConstant.STATUS_PUBLISHED);
            query.setViewPermission(ArticleConstant.VIEW_PERMISSION_ANYBODY);
        }
        return BaseResponse.ok("ok",articleService.queryBy(query,newPageable));
    }

    @ApiOperation("后台查询文章信息")
    @GetMapping("/detail")
    public BaseResponse<ArticleDTO> getArticleDetail(@Validated @NotNull Long id){
        return BaseResponse.ok("ok",articleService.findDetailById(id));
    }


    @ApiOperation("查询发布/草稿/删除文章数量")
    @GetMapping("/count")
    public BaseResponse<Map<String, Long>> countByLabel(){
        return BaseResponse.ok("ok", articleService.countByLabel());
    }

    @ApiOperation("新建文章")
    @PostMapping
    public BaseResponse<?> createArticle(){
        articleService.save(new ArticleDTO());
        return BaseResponse.ok("创建文章成功");
    }

    @ApiOperation("更新文章")
    @PutMapping
    public BaseResponse<?> updateArticle(@RequestBody ArticleDTO article){
        Article a = articleService.save(article);
        return BaseResponse.ok("更新文章成功",a.getId());
    }

    @ApiOperation("更新文章删除状态")
    @PutMapping("/delete")
    public BaseResponse<?> updateDeleted(@RequestBody ArticleDTO articleDTO){
        return BaseResponse.ok("ok",articleService.updateDeleted(articleDTO.getId(), articleDTO.getDeleted()));
    }

    @ApiOperation("更新文章分类")
    @PutMapping("/category")
    public BaseResponse<?> updateCategory(@RequestBody ArticleDTO articleDTO){
        boolean res = articleService.updateCategory(articleDTO.getId(), categoryMapper.toEntity(articleDTO.getCategory()));
        return BaseResponse.ok("ok", res);
    }

    @ApiOperation("彻底删除文章")
    @DeleteMapping
    public BaseResponse<?> delete(@RequestBody Long id){
        return BaseResponse.ok("ok",articleService.deleteById(id));
    }

    private Pageable andDefaultPageable(Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC, "priority");
        Sort newSort = sort.and(pageable.getSort());
        return PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),newSort);
    }
}
