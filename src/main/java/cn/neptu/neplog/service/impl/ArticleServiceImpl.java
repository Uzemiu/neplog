package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.dto.TagDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.*;

import static cn.neptu.neplog.constant.ArticleConstant.STATUS_PUBLISHED;
import static cn.neptu.neplog.constant.ArticleConstant.VIEW_PERMISSION_PRIVATE;

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

    @Transactional
    @Override
    public Article save(ArticleDTO articleDTO) {

        if(articleDTO.getTags() != null){
            for(TagDTO tag: articleDTO.getTags()){
                if(tag.getId() == null && StringUtils.hasText(tag.getTag())){
                    Tag newTag = new Tag();
                    newTag.setTag(tag.getTag());

                    // 防止tag重名
                    tagRepository.findByTag(tag.getTag())
                            .orElseGet(() -> tagRepository.save(newTag));
                    tag.setId(newTag.getId());
                }
            }
        }

        Article article = articleMapper.toEntity(articleDTO);

        article.getTags().remove(new Tag()); // 删除id为null的tag

        if(article.getCategory() != null
                && article.getCategory().getId() == null){
            if(StringUtils.hasText(article.getCategory().getName())){
                // 防止category重名
                Category newCategory = categoryService.createIfNotExist(article.getCategory().getName());
                article.setCategory(newCategory);
            } else {
                article.setCategory(null);
            }
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
    public PageDTO<ArticleDTO> search(String content) {

        Specification<Article> specification = (Specification<Article>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(content)){
                Predicate cLike = criteriaBuilder.like(root.get("content"),"%" + content + "%");
                Predicate tLike = criteriaBuilder.like(root.get("title"),"%" + content + "%");
                predicates.add(criteriaBuilder.or(cLike,tLike));

                Join<Article, Tag> join = root.join("tags", JoinType.INNER);
                Predicate tagsLike = criteriaBuilder.like(join.get("tag"), "%"+content+"%");
                predicates.add(criteriaBuilder.or(tagsLike));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return null;
    }

    @Override
    public long countInvisibleArticlesByTag(Long tagId) {
        return articleRepository.countByTags(
                tagId, STATUS_PUBLISHED, VIEW_PERMISSION_PRIVATE, false);
    }

    @Override
    public Map<String, Long> countByLabel() {
        Map<String, Long> res = new HashMap<>();
        res.put("published", articleRepository.countByStatus(STATUS_PUBLISHED));
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

        // 检查是否有阅读权限
        if(!SecurityUtil.isOwner()){
            if (article.getDeleted()
                    || article.getStatus().equals(ArticleConstant.STATUS_DRAFT)
                    || article.getViewPermission().equals(ArticleConstant.VIEW_PERMISSION_PRIVATE)) {
                throw new ResourceNotFoundException("该文章已被删除或未公开");
            }
        }

        ArticleDTO detailDTO = articleMapper.toDto(article);
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
