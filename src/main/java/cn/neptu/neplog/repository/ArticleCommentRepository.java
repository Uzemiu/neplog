package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCommentRepository extends BaseRepository<ArticleComment, Long>, JpaSpecificationExecutor<ArticleComment> {

    long countByArticleId(Long articleId);

    long deleteByArticleId(Long articleId);

    List<ArticleComment> findByArticleId(Long articleId);

    List<ArticleComment> findByArticleIdAndStatus(Long articleId, Integer status);

    List<ArticleComment> findByFatherId(Long fatherId);
}
