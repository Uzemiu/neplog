package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Article;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends BaseRepository<Article,Integer>, JpaSpecificationExecutor<Article> {
}
