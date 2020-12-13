package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer>, JpaSpecificationExecutor<Article> {
}
