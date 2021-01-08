package cn.neptu.neplog.service.impl;


import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.dto.CommentAuthorDTO;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.query.ArticleCommentQuery;
import cn.neptu.neplog.repository.ArticleCommentRepository;
import cn.neptu.neplog.service.ArticleCommentService;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.CommentMapper;
import cn.neptu.neplog.utils.SecurityUtil;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("commentServiceImpl")
public class ArticleCommentServiceImpl extends AbstractCrudService<ArticleComment, Long> implements ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final CommentMapper commentMapper;
    private final ArticleService articleService;

    protected ArticleCommentServiceImpl(ArticleCommentRepository repository,
                                        CommentMapper commentMapper,
                                        ArticleService articleService) {
        super(repository);
        this.articleCommentRepository = repository;
        this.commentMapper = commentMapper;
        this.articleService = articleService;
    }

    @Transactional
    @Override
    public long deleteByArticleId(Long articleId) {
        long count = articleCommentRepository.deleteByArticleId(articleId);
        articleService.updateComments(articleId,count);
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
    public List<CommentDTO> buildSimpleCommentTree(List<CommentDTO> allComments) {
        Map<Long, CommentDTO> map = buildCommentMap(allComments);

        List<CommentDTO> topComments = new ArrayList<>();
        allComments.forEach(comment -> {
            if(comment.getFatherId() == null){
               topComments.add(comment);
            } else {
                CommentDTO father = map.get(comment.getFatherId());
                if(father != null) {
                    comment.setFather(new CommentAuthorDTO(father.getAvatar(),father.getNickname(),father.getLink()));
                    father.getChildren().add(comment);
                }
            }
        });

        return topComments;
    }

    @Override
    public CommentDTO flatMap(CommentDTO fatherComment){
        return flatMap(Lists.list(fatherComment), 2).get(0);
    }

    @Override
    public List<CommentDTO> flatMap(List<CommentDTO> topComments){
        return flatMap(topComments, 2);
    }

    @Override
    public List<CommentDTO> flatMap(List<CommentDTO> commentDTOList, int level){
        List<CommentDTO> result = new ArrayList<>();
        if(level > 1){
            commentDTOList.forEach(commentDTO -> {
                flatMap(commentDTO.getChildren(), level - 1);
            });
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
    public List<CommentDTO> listByArticleIdAndStatus(Long articleId, Integer status) {
        return commentMapper.toDto(articleCommentRepository.findByArticleIdAndStatus(articleId,status));
    }

    @Override
    public List<CommentDTO> queryBy(ArticleCommentQuery query, Pageable pageable) {
        List<ArticleComment> comments = articleCommentRepository.findAll(query.toSpecification(),pageable).toList();
        return commentMapper.toDto(comments);
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

    @Transactional
    @Override
    public ArticleComment deleteById(Long id) {
        ArticleComment comment = getById(id).orElse(null);
        if(comment == null){
            return null;
        }
        CommentDTO withChildren = fillChildren(comment);
        List<Long> childrenIds = flatMap(withChildren).getChildren()
                .stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        childrenIds.add(comment.getId());

        long count = deleteByIdIn(childrenIds);
        articleService.updateComments(comment.getArticleId(), -count);

        return comment;
    }

    private Map<Long, CommentDTO> buildCommentMap(List<CommentDTO> commentDTOList){
        Map<Long, CommentDTO> map = new HashMap<>(commentDTOList.size());
        commentDTOList.forEach(comment -> map.put(comment.getId(), comment));
        return map;
    }

    private CommentDTO fillChildren(ArticleComment comment){
        List<CommentDTO> allComments = listByArticleId(comment.getArticleId());
        Map<Long, CommentDTO> map = buildCommentMap(allComments);
        allComments.forEach(co -> {
            CommentDTO father = map.get(co.getFatherId());
            if(father != null) {
                co.setFather(new CommentAuthorDTO(father.getAvatar(),father.getNickname(),father.getLink()));
                father.getChildren().add(co);
            }
        });
        return map.get(comment.getId());
    }

}
