package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.CategoryDTO;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.query.CategoryQuery;
import cn.neptu.neplog.service.base.CrudService;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends CrudService<Category, Long> {

    Category createByName(String name);

    Category createIfNotExist(String name);

    Optional<Category> getByName(String name);

    List<Category> deleteByParentId(Long parentId);

    List<CategoryDTO> queryBy(CategoryQuery query);

}
