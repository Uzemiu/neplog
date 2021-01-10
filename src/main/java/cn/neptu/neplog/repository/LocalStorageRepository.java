package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.LocalStorage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalStorageRepository extends BaseRepository<LocalStorage, Long>{

    List<LocalStorage> findByName(String name);
}
