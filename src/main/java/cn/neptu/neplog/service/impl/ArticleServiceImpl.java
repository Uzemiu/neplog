package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.params.query.ArticleQuery;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.mapstruct.ArticleBaseMapper;
import cn.neptu.neplog.service.mapstruct.ArticleMapper;
import cn.neptu.neplog.service.mapstruct.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("articleService")
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleBaseMapper articleBaseMapper;
    private final CategoryMapper categoryMapper;
    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    public Article findById(Integer id) {
        return articleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    @Override
    public Article save(ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);

        String categoryName = articleDTO.getCategory();
        if(StringUtils.hasText(categoryName)){
            Category category = categoryService
                    .findByName(categoryName)
                    .orElseGet(() -> categoryService.save(new Category(null,categoryName)));
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
    public List<ArticleBaseDTO> queryBy(ArticleQuery query, Pageable pageable) {
        List<Article> articles = articleRepository.findAll(query.toSpecification(),pageable).toList();
        return articles.stream().map(article -> {
            ArticleBaseDTO dto = articleBaseMapper.toDto(article);
            fillProperties(dto,article);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> countByLabel() {
        return null;
    }

    @Override
    public boolean trash(Integer id, Boolean deleted) {
        return articleRepository.updateDeleted(id, deleted) > 0;
    }

    @Override
    public ArticleBaseDTO findViewById(Integer id) {
        Article article = findById(id);
        ArticleBaseDTO viewDTO = articleBaseMapper.toDto(article);
        viewDTO.setHtmlContent(article.getHtmlContent());
        fillProperties(viewDTO,article);
        return viewDTO;
    }

    @Override
    public ArticleDTO findDetailById(Integer id) {
        Article article = findById(id);
        ArticleDTO detailDTO = articleMapper.toDto(article);
        fillProperties(detailDTO,article);
        return detailDTO;
    }

    @Override
    public void increaseVisit(String id, Long increment) {
        articleRepository.updateViews(Integer.valueOf(id),increment);
    }

    private void fillProperties(ArticleBaseDTO dto, Article article){
        dto.setTags(tagService.findByArticleId(article.getId()).stream().map(Tag::getTag).collect(Collectors.toList()));
        dto.setCategory(categoryService.findById(article.getCategoryId()).get().getName());
    }
}
