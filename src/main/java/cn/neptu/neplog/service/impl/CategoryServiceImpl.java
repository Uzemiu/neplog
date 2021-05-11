package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.dto.CategoryDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.query.CategoryQuery;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.repository.CategoryRepository;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service("categoryService")
public class CategoryServiceImpl
        extends AbstractCrudService<Category, Long>
        implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper mapper,
                               ArticleRepository articleRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.articleRepository = articleRepository;
    }

    @Override
    public Optional<Category> getByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category createByName(String name) {
        Category newCategory = new Category();
        if(StringUtils.hasText(name)){
            newCategory.setName(name);
            return categoryRepository.save(newCategory);
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
        newCategory.setName("未命名" + (max + 1));
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category createIfNotExist(String name) {
        return getByName(name).orElseGet(() -> createByName(name));
    }

    @Override
    public List<CategoryDTO> queryBy(CategoryQuery query) {
        List<CategoryDTO> categoryDTOS = mapper.toDto(categoryRepository.findAll(query.toSpecification()));
        categoryDTOS.forEach(categoryDTO -> categoryDTO.setArticleCount(
                categoryRepository.getArticleCount(categoryDTO.getId())));
        return categoryDTOS;
    }

    @Override
    @Transactional
    public List<Category> deleteByParentId(Long parentId) {
        List<Category> categories = categoryRepository.deleteByParentId(parentId);
        categories.forEach(category -> deleteByParentId(category.getId()));
        return categories;
    }

    @Override
    @Transactional
    public Category deleteById(Long id) {
        Category parent = getNotNullById(id);

        List<Category> toBeDeleted = new LinkedList<>();
        toBeDeleted.add(parent);
        findChildren(parent, toBeDeleted);
        articleRepository.unlinkCategory(toBeDeleted);
        categoryRepository.deleteAll(toBeDeleted);

        return parent;
    }

    private void findChildren(Category category, List<Category> res){
        List<Category> children = categoryRepository.findByParentId(category.getId());
        children.forEach(c -> {
            res.add(c);
            findChildren(c, res);
        });
    }
}
