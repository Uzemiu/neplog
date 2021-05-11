package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.repository.ArticleCommentRepository;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.repository.TagRepository;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.ArticleMapper;
import cn.neptu.neplog.utils.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("articleService")
public class ArticleServiceImpl extends AbstractCrudService<Article, Long> implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final CategoryService categoryService;

    protected ArticleServiceImpl(ArticleMapper articleMapper,
                                 ArticleRepository repository,
                                 ArticleCommentRepository commentRepository,
                                 CategoryService categoryService,
                                 TagRepository tagRepository) {
        super(repository);
        this.articleMapper = articleMapper;
        this.articleRepository = repository;
        this.commentRepository = commentRepository;
        this.categoryService = categoryService;
        this.tagRepository = tagRepository;
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    @Override
    public Article save(ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);

        if(article.getCategory() != null
                && article.getCategory().getId() == null){
            // 防止category重名
            categoryService.getByName(article.getCategory().getName()).ifPresent(article::setCategory);
        }
        if(article.getTags() != null){
            article.getTags().forEach(tag -> {
                if(tag.getId() == null){
                    // 防止tag重名
                    tagRepository.findByTag(tag.getTag())
                            .ifPresent(old -> tag.setId(old.getId()));
                }
            });
        }
        articleRepository.save(article);

        return article;
    }

    @Override
    public long updateLikes(Long id, Long increment) {
        return articleRepository.updateLikes(id, increment);
    }

    @Override
    public long updateComments(Long id, Long increment) {
        return articleRepository.updateComments(id, increment);
    }

    @Override
    public void increaseVisit(String id, Long increment) {
        articleRepository.updateViews(Long.valueOf(id), increment);
    }

    @Override
    public PageDTO<ArticleDTO> queryBy(ArticleQuery query, Pageable pageable) {
        Page<Article> a = articleRepository.findAll(query.toSpecification(), pageable);
        return new PageDTO<>(a.map(articleMapper::toDto));
    }

    @Override
    public Map<String, Long> countByLabel() {
        Map<String, Long> res = new HashMap<>();
        res.put("published", articleRepository.countByStatus(ArticleConstant.STATUS_PUBLISHED));
        res.put("draft", articleRepository.countByStatus(ArticleConstant.STATUS_DRAFT));
        res.put("deleted", articleRepository.countByDeleted(true));
        return res;
    }

    @Override
    public boolean updateDeleted(Long id, Boolean deleted) {
        return articleRepository.updateDeleted(id, deleted) > 0;
    }

    @Override
    public boolean updateCategory(Long id, Category category) {
        return articleRepository.updateCategory(id, category) > 0;
    }

    @Override
    public ArticleDTO findViewById(Long id) {
        Article article = getNotNullById(id);

        ArticleDTO viewDTO = articleMapper.toDto(article);
        viewDTO.setHtmlContent(article.getHtmlContent());
        // 检查是否有阅读权限
        if(!SecurityUtil.isOwner()){
            if (article.getDeleted()
                    || article.getStatus().equals(ArticleConstant.STATUS_DRAFT)
                    || article.getViewPermission().equals(ArticleConstant.VIEW_PERMISSION_PRIVATE)) {
                throw new ResourceNotFoundException("该文章已被删除或未公开");
            }

            if (article.getViewPermission().equals(ArticleConstant.VIEW_PERMISSION_USER_ONLY)
                    && !SecurityUtil.isLogin()) {
                viewDTO.setHtmlContent(null);
            }
        }

        return viewDTO;
    }

    @Override
    public ArticleDTO findDetailById(Long id) {
        Article article = getNotNullById(id);
        ArticleDTO detailDTO = articleMapper.toDto(article);

        // 检查是否有阅读权限
        if(!SecurityUtil.isOwner()){
            if (article.getDeleted()
                    || article.getStatus().equals(ArticleConstant.STATUS_DRAFT)
                    || article.getViewPermission().equals(ArticleConstant.VIEW_PERMISSION_PRIVATE)) {
                throw new ResourceNotFoundException("该文章已被删除或未公开");
            }
        }

        detailDTO.setContent(article.getContent());
        detailDTO.setHtmlContent(article.getHtmlContent());
        return detailDTO;
    }

    @Transactional
    @Override
    public Article deleteById(Long id) {
        Article article = super.deleteById(id);
        commentRepository.deleteByArticleId(article.getId());
        return article;
    }

    @Transactional
    @Override
    public long deleteByIdIn(Collection<Long> ids) {
        long count = super.deleteByIdIn(ids);
        commentRepository.deleteByArticleIdIn(ids);
        return count;
    }
}
