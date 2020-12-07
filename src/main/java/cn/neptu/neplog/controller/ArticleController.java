package cn.neptu.neplog.controller;

import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public BaseResponse<?> getArticle(){

        return BaseResponse.ok("",articleService.find());
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
