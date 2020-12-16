package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Article;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ArticleRepository extends BaseRepository<Article,Integer>, JpaSpecificationExecutor<Article> {

    @Modifying
    @Query("update Article a set a.views = a.views + :increment where a.id = :id")
    int updateViews(@Param("id") Integer id, @Param("increment") Integer increment);
}
