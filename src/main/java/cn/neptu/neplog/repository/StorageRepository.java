package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Storage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends BaseRepository<Storage, Long>, JpaSpecificationExecutor<Storage> {

    List<Storage> findByType(String type);

    Optional<Storage> findByVirtualPath(String virtualPath);

    Optional<Storage> findByFilePath(String filePath);
}
