package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.service.base.CrudService;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends CrudService<Category, Integer> {

    Category createByName(String name);

    Category createIfNotExist(String name);

    Optional<Category> getByName(String name);

}
