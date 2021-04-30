package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category,Integer>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);

    List<Category> findByNameLike(String name);

    List<Category> findByParentId(Integer parentId);

    List<Category> deleteByParentId(Integer parentId);

}
