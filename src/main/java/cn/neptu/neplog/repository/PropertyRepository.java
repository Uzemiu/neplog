package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Property;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PropertyRepository extends BaseRepository<Property, Integer>{

    Optional<Property> getByKey(String key);

    Set<Property> getByKeyIn(Collection<String> key);

    Set<Property> getByKeyNotIn(Collection<String> key);

    int deletePropertiesByKeyNotIn(Collection<String> key);

}
