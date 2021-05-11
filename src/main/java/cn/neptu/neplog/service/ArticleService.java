package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.service.base.CrudService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ArticleService extends CrudService<Article, Long> ,VisitService{

    Article save(ArticleDTO article);

    Article save(Article article);

    ArticleDTO findDetailById(Long id);

    ArticleDTO findViewById(Long id);

    boolean updateDeleted(Long id, Boolean deleted);

    boolean updateCategory(Long id, Category category);

    PageDTO<ArticleDTO> queryBy(ArticleQuery query, Pageable pageable);

    long updateLikes(Long id, Long increment);

    long updateComments(Long id, Long increment);

    Map<String, Long> countByLabel();

}
