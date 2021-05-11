package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface ArticleRepository extends BaseRepository<Article,Long>, JpaSpecificationExecutor<Article> {

    @Transactional
    @Modifying
    @Query("update Article a set a.views = a.views + :increment where a.id = :id")
    int updateViews(@Param("id") Long id, @Param("increment") Long increment);

    @Transactional
    @Modifying
    @Query("update Article a set a.likes = a.likes + :increment where a.id = :id")
    int updateLikes(@Param("id") Long id, @Param("increment") Long increment);

    @Transactional
    @Modifying
    @Query("update Article a set a.comments = a.comments + :increment where a.id = :id")
    int updateComments(@Param("id") Long id, @Param("increment") Long increment);

    @Transactional
    @Modifying
    @Query("update Article a set a.deleted = :deleted where a.id = :id")
    int updateDeleted(@Param("id") Long id, @Param("deleted") Boolean deleted);

    @Transactional
    @Modifying
    @Query("update Article a set a.category = :cate where a.id = :id")
    int updateCategory(@Param("id") Long id, @Param("cate") Category category);

    @Query("select count(a) from Article a where a.status = :status and a.deleted = false")
    long countByStatus(@Param("status")Integer status);

    long countByDeleted(Boolean deleted);

    long countByCategory(Category category);

    @Transactional
    @Modifying
    @Query("update Article a set a.category = null where a.category = :cate")
    int unlinkCategory(@Param("cate")Category cate);

    @Transactional
    @Modifying
    @Query("update Article a set a.category = null where a.category in :cates")
    int unlinkCategory(@Param("cates") Collection<Category> cates);
}
