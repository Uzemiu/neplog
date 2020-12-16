package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Property;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends BaseRepository<Property, Integer>{

    Optional<Property> getByKey(String key);

    List<Property> getByKeyIn(Collection<String> key);
}
