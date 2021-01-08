package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.constant.CommentConstant;
import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.repository.ArticleCommentRepository;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.ArticleBaseMapper;
import cn.neptu.neplog.service.mapstruct.ArticleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("articleService")
public class ArticleServiceImpl extends AbstractCrudService<Article, Long> implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleBaseMapper articleBaseMapper;
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository commentRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    protected ArticleServiceImpl(ArticleMapper articleMapper,
                                 ArticleBaseMapper articleBaseMapper,
                                 ArticleRepository repository,
                                 ArticleCommentRepository commentRepository,
                                 CategoryService categoryService,
                                 TagService tagService) {
        super(repository);
        this.articleMapper = articleMapper;
        this.articleBaseMapper = articleBaseMapper;
        this.articleRepository = repository;
        this.commentRepository = commentRepository;
        this.categoryService = categoryService;
        this.tagService = tagService;
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
    public long updateLikes(Long id, Long increment) {
        return articleRepository.updateLikes(id,increment);
    }

    @Override
    public long updateComments(Long id, Long increment) {
        return articleRepository.updateComments(id,increment);
    }

    @Override
    public void increaseVisit(String id, Long increment) {
        articleRepository.updateViews(Long.valueOf(id),increment);
    }

    @Override
    public List<ArticleBaseDTO> queryBy(ArticleQuery query, Pageable pageable) {
        Page<Article> a = articleRepository.findAll(query.toSpecification(),pageable);
        List<Article> articles = a.toList();
        return articles.stream().map(article -> {
            ArticleBaseDTO dto = articleBaseMapper.toDto(article);
            fillProperties(dto,article);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> countByLabel() {
        Map<String, Long> res = new HashMap<>();
        res.put("published",articleRepository.countByStatus(ArticleConstant.STATUS_PUBLISHED));
        res.put("draft",articleRepository.countByStatus(ArticleConstant.STATUS_DRAFT));
        res.put("deleted",articleRepository.countByDeleted(true));
        return res;
    }

    @Override
    public boolean trash(Long id, Boolean deleted) {
        return articleRepository.updateDeleted(id, deleted) > 0;
    }

    @Override
    public ArticleBaseDTO findViewById(Long id) {
        Article article = getNotNullById(id);
        ArticleBaseDTO viewDTO = articleBaseMapper.toDto(article);
        viewDTO.setHtmlContent(article.getHtmlContent());
        fillProperties(viewDTO,article);
        return viewDTO;
    }

    @Override
    public ArticleDTO findDetailById(Long id) {
        Article article = getNotNullById(id);
        ArticleDTO detailDTO = articleMapper.toDto(article);
        fillProperties(detailDTO,article);
        return detailDTO;
    }


    @Transactional
    @Override
    public Article deleteById(Long integer) {
        Article article = super.deleteById(integer);
        commentRepository.deleteByArticleId(article.getId());
        return article;
    }


    private void fillProperties(ArticleBaseDTO dto, Article article){
        dto.setTags(tagService.findByArticleId(article.getId()).stream().map(Tag::getTag).collect(Collectors.toList()));
        dto.setCategory(categoryService.findById(article.getCategoryId()).get().getName());
    }
}
