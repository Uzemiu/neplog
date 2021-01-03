package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.service.base.CrudService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ArticleService extends CrudService<Article, Long> ,VisitService{

    Article save(ArticleDTO article);

    Article save(Article article);

    ArticleDTO findDetailById(Long id);

    ArticleBaseDTO findViewById(Long id);

    boolean trash(Long id, Boolean deleted);

    List<ArticleBaseDTO> queryBy(ArticleQuery query, Pageable pageable);

    Map<String, Long> countByLabel();
}
