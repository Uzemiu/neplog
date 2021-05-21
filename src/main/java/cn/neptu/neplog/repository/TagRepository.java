package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends BaseRepository<Tag,Long> {

    Set<Tag> findByTagIn(Collection<String> tags);

    Optional<Tag> findByTag(String tag);

    @Query("select t.articles.size from Tag t where t.id = :id")
    long getArticleCount(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from article_tag where tag_id = :id", nativeQuery = true)
    void unlinkTag(@Param("id") Long id);
}
