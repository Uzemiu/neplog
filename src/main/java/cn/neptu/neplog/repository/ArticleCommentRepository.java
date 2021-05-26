package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ArticleCommentRepository extends BaseRepository<ArticleComment, Long>, JpaSpecificationExecutor<ArticleComment> {

    long countByArticleId(Long articleId);

    long countByArticleIdAndStatus(Long articleId, Integer status);

    long deleteByArticleId(Long articleId);

    long deleteByArticleIdIn(Collection<Long> articleId);

    List<ArticleComment> findByParentId(Long parentId);

    List<ArticleComment> findByArticleId(Long articleId);

    List<ArticleComment> findByArticleIdAndStatus(Long articleId, Integer status);
}
