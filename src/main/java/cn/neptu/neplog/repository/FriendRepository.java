package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Friend;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface FriendRepository extends BaseRepository<Friend, Long>, JpaSpecificationExecutor<Friend> {

    List<Friend> findByStatus(Integer status);

    @Transactional
    @Modifying
    @Query("update Friend f set f.status=:status where f.id in :ids")
    void updateStatusByIdIn(@Param("ids") Collection<Long> ids, @Param("status") Integer status);

    long countByStatus(Integer status);
}
