package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User,String> {

    Optional<User> getByUsername(String username);

    Optional<User> getByLevel(Integer level);

}
