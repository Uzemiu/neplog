package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.config.BlogConfig;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BlogConfigRepository extends BaseRepository<BlogConfig, Long>{

    @Transactional
    @Modifying
    @Query("update BlogConfig c set c.visitCount = c.visitCount + :increment")
    int updateViews(@Param("increment") Long increment);
}
