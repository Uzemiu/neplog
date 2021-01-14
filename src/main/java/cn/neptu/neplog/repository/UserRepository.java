package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User,String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByLevel(Integer level);

}
