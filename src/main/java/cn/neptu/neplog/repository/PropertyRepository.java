package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Property;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PropertyRepository extends BaseRepository<Property, Integer>{

    Optional<Property> findByKey(String key);

    @Query("select p.value from Property p where p.key = :key")
    String findValueByKey(@Param("key") String key);

    Set<Property> findByKeyIn(Collection<String> key);

    Set<Property> findByKeyNotIn(Collection<String> key);

    int deletePropertiesByKeyNotIn(Collection<String> key);

}
