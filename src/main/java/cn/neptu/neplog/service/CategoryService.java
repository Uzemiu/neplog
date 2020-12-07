package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Category;

import java.util.Optional;

public interface CategoryService {

    Category save(Category category);

    Optional<Category> findByName(String name);
}
