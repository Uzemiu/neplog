package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.params.query.ArticleQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/detail")
    public BaseResponse<ArticleDTO> getArticleDetail(Integer id){
        return BaseResponse.ok("ok",articleService.findDetailById(id));
    }

    @GetMapping("/view")
    @AnonymousAccess
    public BaseResponse<ArticleBaseDTO> getArticleView(Integer id){
        return BaseResponse.ok("ok",articleService.findViewById(id));
    }

    @GetMapping
    @AnonymousAccess
    public BaseResponse<List<ArticleBaseDTO>> queryBy(@Validated ArticleQuery query,
                                                     @PageableDefault(sort = {"priority","createTime"},
                                                    direction = Sort.Direction.DESC) Pageable pageable){
        User user = SecurityUtil.getCurrentUser();
        // 非博主不允许搜索已删除内容
        if(query.getDeleted() !=null && query.getDeleted() && (user == null || user.getLevel() < 6)){
            throw new BadRequestException("Illegal parameter");
        }
        return BaseResponse.ok("ok",articleService.queryBy(query,pageable));
    }

    @PostMapping
    public BaseResponse<Article> createArticle(){
        return BaseResponse.ok("创建文章成功",articleService.save(new ArticleDTO()));
    }

    @PutMapping
    public BaseResponse<?> updateArticle(@RequestBody ArticleDTO article){
        articleService.save(article);
        return BaseResponse.ok("更新文章成功");
    }
}
