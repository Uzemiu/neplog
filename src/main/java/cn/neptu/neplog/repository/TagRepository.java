package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {

    Set<Tag> findByTagIn(Collection<String> tag);

    int deleteByArticleIdAndTagNotIn(Integer articleId, Collection<String> tag);

}