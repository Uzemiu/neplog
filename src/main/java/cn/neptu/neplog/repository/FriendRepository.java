package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Friend;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends BaseRepository<Friend, Integer>, JpaSpecificationExecutor<Friend> {

    List<Friend> findByStatus(Integer status);

    long countByStatus(Integer status);
}
