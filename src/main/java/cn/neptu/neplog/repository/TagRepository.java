package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends BaseRepository<Tag,Long> {

    Set<Tag> findByTagIn(Collection<String> tags);

    Optional<Tag> findByTag(String tag);
}
