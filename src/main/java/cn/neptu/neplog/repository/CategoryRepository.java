package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category,Long>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);

    List<Category> findByNameLike(String name);

    List<Category> findByParentId(Long parentId);

    List<Category> deleteByParentId(Long parentId);

    @Query("select c.articles.size from Category c where c.id = :id")
    long getArticleCount(@Param("id") Long id);

}
