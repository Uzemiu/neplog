package cn.neptu.neplog.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.constant.CommentConstant;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.query.ArticleCommentQuery;
import cn.neptu.neplog.repository.ArticleCommentRepository;
import cn.neptu.neplog.repository.UserRepository;
import cn.neptu.neplog.service.ArticleCommentService;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.CommentMapper;
import cn.neptu.neplog.utils.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Service("commentServiceImpl")
public class ArticleCommentServiceImpl
        extends AbstractCrudService<ArticleComment, Long>
        implements ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final ArticleService articleService;

    protected ArticleCommentServiceImpl(ArticleCommentRepository repository,
                                        UserRepository userRepository,
                                        CommentMapper commentMapper,
                                        ArticleService articleService) {
        super(repository);
        this.articleCommentRepository = repository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.articleService = articleService;
    }

    @Transactional
    @Override
    public long deleteByArticleId(Long articleId) {
        long count = articleCommentRepository.deleteByArticleId(articleId);
        articleService.updateComments(articleId, count);
        return count;
    }

    @Override
    public long countByArticleId(Long articleId) {
        return articleCommentRepository.countByArticleId(articleId);
    }

    @Override
    public long countByArticleIdAndStatus(Long articleId, Integer status) {
        return articleCommentRepository.countByArticleIdAndStatus(articleId,status);
    }

    @Override
    public CommentDTO flatMap(CommentDTO fatherComment){
        return flatMap(Collections.singletonList(fatherComment), 2).get(0);
    }

    @Override
    public List<CommentDTO> flatMap(List<CommentDTO> topComments){
        return flatMap(topComments, 2);
    }

    @Override
    public List<CommentDTO> flatMap(List<CommentDTO> commentDTOList, int level){
        List<CommentDTO> result = new ArrayList<>();
        if(level > 1){
            commentDTOList.forEach(commentDTO -> flatMap(commentDTO.getChildren(), level - 1));
        } else {
            commentDTOList.forEach(commentDTO -> {
                result.addAll(flatMap(commentDTO.getChildren(), 0));
                commentDTO.getChildren().clear();
            });
        }
        commentDTOList.addAll(result);
        return commentDTOList;
    }

    @Override
    public List<CommentDTO> listByArticleId(Long articleId) {
        return commentMapper.toDto(articleCommentRepository.findByArticleId(articleId));
    }

    @Override
    public List<CommentDTO> listPublicByArticleId(Long articleId) {
        return commentMapper.toDto(articleCommentRepository.findByArticleIdAndStatus(articleId, CommentConstant.STATUS_PUBLIC));
    }

    @Override
    public PageDTO<CommentDTO> queryBy(ArticleCommentQuery query, Pageable pageable) {
        Page<CommentDTO> comments = articleCommentRepository.findAll(query.toSpecification(),pageable).map(commentMapper::toDto);
        return new PageDTO<>(comments);
    }

    @Override
    public PageDTO<CommentDTO> queryByWithArticle(ArticleCommentQuery query, Pageable pageable) {
        Page<ArticleComment> comments = articleCommentRepository.findAll(query.toSpecification(), pageable);
        Page<CommentDTO> commentDTOS = comments.map(comment -> {
            CommentDTO dto = commentMapper.toDto(comment);
            dto.setArticle(articleService.findViewById(comment.getArticleId()));
            return dto;
        });
        return new PageDTO<>(commentDTOS);
    }

    @Transactional
    @Override
    public ArticleComment create(CommentDTO commentDTO) {
        ArticleComment comment = commentMapper.toEntity(commentDTO);
        Integer cp = articleService.getNotNullById(comment.getArticleId()).getCommentPermission();

        Assert.isTrue( cp < ArticleConstant.COMMENT_PERMISSION_CLOSED, "该文章已关闭评论");
        if(cp >= ArticleConstant.COMMENT_PERMISSION_USER_ONLY && !SecurityUtil.isLogin()){
            throw new BadRequestException("仅登录用户可以评论该文章");
        } else {
            comment.setStatus(cp >= ArticleConstant.COMMENT_PERMISSION_REQUIRE_REVIEW ? 0 : 1);
        }

        User user = SecurityUtil.getCurrentUser();
        if(user != null){
            comment.setUserId(user.getId());
            comment.setNickname(user.getNickname());
            comment.setAvatar(user.getAvatar());
            comment.setEmail(user.getEmail());
        }

        articleCommentRepository.save(comment);
        articleService.updateComments(comment.getArticleId(), 1L);

        return comment;
    }

    @Override
    public void fillChildren(CommentDTO commentDTO) {
        fillChildren(Collections.singletonList(commentDTO), commentDTO.getArticleId());
    }

    @Override
    public void fillChildren(Iterable<CommentDTO> commentDTOS, Long articleId) {
        Assert.notNull(articleId, "Article id must not be null");
        if(CollectionUtil.isEmpty(commentDTOS)){
            return;
        }
        // 一次性查找文章下所有评论来构建评论树
        List<CommentDTO> allComments = commentMapper.toDto(articleCommentRepository.findByArticleId(articleId));

        // 覆盖map中查询到的评论使得能够直接在map中构建子树
        Map<Long, CommentDTO> map = buildCommentMap(allComments);
        for(CommentDTO dto: commentDTOS){
            map.replace(dto.getId(), dto);
        }

        for(CommentDTO dto: allComments){
            if(dto.getParentId() == null){
                continue;
            }
            CommentDTO father = map.get(dto.getParentId());
            if(father != null) {
                father.getChildren().add(dto);
            }
        }
    }

    @Override
    public void setUserInfo(Iterable<CommentDTO> dtos){
        if(dtos == null){
            return;
        }
        for(CommentDTO dto: dtos){
            setUserInfo(dto);
            setUserInfo(dto.getChildren());
        }
    }

    @Override
    public void setUserInfo(CommentDTO dto){
        String userId = dto.getUserId();
        if(userId != null && userId.contains("-")){
            userRepository.findById(userId)
                    .ifPresent(user -> {
                        dto.setNickname(user.getNickname());
                        dto.setAvatar(user.getAvatar());
                        dto.setLink(user.getSite());
                    });
        }
    }

    @Transactional
    @Override
    public ArticleComment deleteById(Long id) {
        ArticleComment comment = getById(id).orElse(null);
        if(comment == null){
            return null;
        }
        Set<ArticleComment> children = listChildren(comment);
        children.add(comment);

        articleCommentRepository.deleteAll(children);
        articleService.updateComments(comment.getArticleId(), (long) -children.size());

        return comment;
    }

    @Transactional
    @Override
    public long deleteByIdIn(Collection<Long> longs) {
        long count = 0L;
        for (Long aLong : longs) {
           count += deleteById(aLong) == null ? 0 : 1;
        }
        return count;
    }

    private Map<Long, CommentDTO> buildCommentMap(List<CommentDTO> commentDTOList){
        Map<Long, CommentDTO> map = new HashMap<>(commentDTOList.size());
        commentDTOList.forEach(comment -> map.put(comment.getId(), comment));
        return map;
    }

    /**
     * 通过递归以列表的方式查询comment下所有子评论
     * @param comment /
     * @return 所有子评论
     */
    private Set<ArticleComment> listChildren(ArticleComment comment){
        Assert.notNull(comment, "comment must not be null");
        Set<ArticleComment> children = new HashSet<>();
        List<ArticleComment> directChildren =
                articleCommentRepository.findByParentId(comment.getId());
        listChildren(directChildren, children);
        return children;
    }

    private void listChildren(List<ArticleComment> comments, Set<ArticleComment> children){
        Assert.notNull(children, "children must not be null");
        if(comments == null){
            return;
        }
        children.addAll(comments);
        for(ArticleComment comment: comments){
            List<ArticleComment> directChildren =
                    articleCommentRepository.findByParentId(comment.getId());
            listChildren(directChildren, children);
        }
    }

}
