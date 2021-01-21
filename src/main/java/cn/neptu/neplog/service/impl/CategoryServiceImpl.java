package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.repository.CategoryRepository;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service("categoryService")
public class CategoryServiceImpl extends AbstractCrudService<Category, Integer> implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> getByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category createByName(String name) {
        if(StringUtils.hasText(name)){
            return categoryRepository.save(new Category(null, name));
        }
        List<Category> categories = categoryRepository.findByNameLike("未命名%");
        int max = 0;
        for(Category category : categories){
            if(category.getName().length() > 3){
                try{
                    max = Math.max(Integer.parseInt(category.getName().substring(3)), max);
                } catch (Exception ignored){}
            }
        }
        return categoryRepository.save(new Category(null, "未命名" + (max + 1)));
    }

    @Override
    public Category createIfNotExist(String name) {
        return getByName(name).orElseGet(() -> createByName(name));
    }

}
