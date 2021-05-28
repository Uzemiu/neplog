package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.BaseLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface BaseLikeRepository<LIKE extends BaseLike> extends BaseRepository<LIKE, Long> {

    LIKE findByTargetIdAndIdentity(Long targetId, String identity);
}
