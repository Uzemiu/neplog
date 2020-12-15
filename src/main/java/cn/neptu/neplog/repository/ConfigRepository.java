package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Config;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepository extends BaseRepository<Config, Integer>{

    Optional<Config> getByKey(String key);

    List<Config> getByKeyIn(Collection<String> key);
}
