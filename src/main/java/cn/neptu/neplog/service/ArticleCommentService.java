package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.model.query.ArticleCommentQuery;
import cn.neptu.neplog.service.base.CrudService;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleCommentService extends CrudService<ArticleComment, Long> {

    long countByArticleId(Long articleId);

    long deleteByArticleId(Long articleId);

    ArticleComment create(CommentDTO commentDTO);

    List<CommentDTO> listByArticleId(Long articleId);

    List<CommentDTO> queryBy(ArticleCommentQuery query, Pageable pageable);

    /**
     * 构建评论树结构并注入父评论作者信息
     * @param allComments 文章下所有的评论
     * @return 顶层评论（直接回复文章的论文）
     */
    List<CommentDTO> buildSimpleCommentTree(List<CommentDTO> allComments);

    /**
     * 展开单个评论的所有子评论（默认展开到1级子评论）
     * @param fatherComment 父评论
     * @return 包括评论的所有子评论的父评论
     */
    CommentDTO flatMap(CommentDTO fatherComment);

    /**
     * 展开所有顶层评论的所有子评论（默认展开到1级子评论）
     * @param topComments 顶层评论
     * @return 展开后的顶层评论列表
     */
    List<CommentDTO> flatMap(List<CommentDTO> topComments);

    /**
     * 展开所有顶层评论的所有子评论
     * @param topComments 顶层评论
     * @param level 展开level级子评论
     * @return 展开后的顶层评论列表
     */
    List<CommentDTO> flatMap(List<CommentDTO> topComments, int level);
}
