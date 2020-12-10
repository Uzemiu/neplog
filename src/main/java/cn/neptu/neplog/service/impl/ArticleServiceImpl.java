package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.dto.CategoryDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.mapstruct.ArticleMapper;
import cn.neptu.neplog.service.mapstruct.BaseArticleMapper;
import cn.neptu.neplog.service.mapstruct.CategoryMapper;
import cn.neptu.neplog.service.mapstruct.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service("articleService")
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Transactional
    @Override
    public Article save(ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);

        CategoryDTO categoryDTO = articleDTO.getCategory();
        if(categoryDTO.getId() == null){
            Category category = categoryService.findByName(categoryDTO.getName())
                    .orElseGet(() -> new Category(null,categoryDTO.getName()));
            categoryService.save(category);
            article.setCategoryId(category.getId());
        }
        articleRepository.save(article);

        List<Tag> newTags = articleDTO.getTags().stream().map(tag -> new Tag(null,article.getId(),tag)).collect(Collectors.toList());
        Set<Tag> exist = tagService.findByArticleId(article.getId());
        exist.addAll(newTags);
        tagService.saveAll(exist);
        tagService.deleteTagsNotIn(article.getId(),exist.stream().map(Tag::getTag).collect(Collectors.toList()));

        return article;
    }

    @Override
    public ArticleDTO findById(Integer id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        return toDTO(article);
    }

    private ArticleDTO toDTO(Article article){
        ArticleDTO articleDTO = articleMapper.toDto(article);
        articleDTO.setTags(tagService.findByArticleId(article.getId()).stream().map(Tag::getTag).collect(Collectors.toList()));
        articleDTO.setCategory(categoryMapper.toDto(categoryService.findById(article.getCategoryId()).get()));
        return articleDTO;
    }
}
