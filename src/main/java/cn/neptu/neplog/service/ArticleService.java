package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.params.query.ArticleQuery;
import cn.neptu.neplog.service.base.CrudService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ArticleService extends CrudService<Article, Integer> ,VisitService{

    Integer STATUS_DRAFT = 0;

    Integer STATUS_PUBLISHED = 4;

    Article save(ArticleDTO article);

    Article save(Article article);

    ArticleDTO findDetailById(Integer id);

    ArticleBaseDTO findViewById(Integer id);

    boolean trash(Integer id, Boolean deleted);

    List<ArticleBaseDTO> queryBy(ArticleQuery query, Pageable pageable);

    Map<String, Long> countByLabel();
}
