package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.ArticleComment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends BaseRepository<ArticleComment, Integer>{

    int countByArticleId(Integer articleId);

    List<ArticleComment> findByArticleId(Integer articleId);
    
    List<ArticleComment> findByArticleIdOrderByIdAsc(Integer articleId);
}
