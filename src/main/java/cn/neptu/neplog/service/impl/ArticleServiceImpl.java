package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.mapstruct.ArticleMapper;
import cn.neptu.neplog.service.mapstruct.BaseArticleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("articleService")
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    public List<Article> find() {
        return articleRepository.findAll();
    }

    @Transactional
    @Override
    public Article save(ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);

        if(articleDTO.getCategory().getId() == null){
            Category category = categoryService.save(articleDTO.getCategory());
            article.setCategoryId(category.getId());
        } else {
            article.setCategoryId(articleDTO.getCategory().getId());
        }
        articleRepository.save(article);

        Set<Tag> newTags = articleDTO.getTags();
        if (newTags != null) {
            for(Tag tag : newTags){
                tag.setArticleId(article.getId());
            }
            Set<Tag> exist = tagService.findTagIn(newTags.stream().map(Tag::getTag).collect(Collectors.toList()));
            exist.addAll(newTags);
            tagService.saveAll(exist);
            tagService.deleteTagsNotIn(article.getId(),exist.stream().map(Tag::getTag).collect(Collectors.toList()));
        }

        return article;
    }
}
