package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.params.query.ArticleQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ArticleService extends VisitService{

    Article save(ArticleDTO article);

    Article save(Article article);

    Article findById(Integer id);

    ArticleDTO findDetailById(Integer id);

    ArticleBaseDTO findViewById(Integer id);

    boolean trash(Integer id, Boolean deleted);

    List<ArticleBaseDTO> queryBy(ArticleQuery query, Pageable pageable);

    Map<String, Long> countByLabel();
}
