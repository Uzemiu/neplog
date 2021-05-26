package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.model.query.ArticleCommentQuery;
import cn.neptu.neplog.service.base.CrudService;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticleCommentService extends CrudService<ArticleComment, Long> {

    long countByArticleId(Long articleId);

    long countByArticleIdAndStatus(Long articleId, Integer status);

    long deleteByArticleId(Long articleId);

    ArticleComment create(CommentDTO commentDTO);

    void fillChildren(CommentDTO commentDTO);

    /**
     * 通过一次性查找ArticleId下所有评论构建ID映射的Map
     * 将每个CommentDTO下的所有子评论以树结构的形式填充
     * @param commentDTOS /
     * @param articleId /
     */
    void fillChildren(Iterable<CommentDTO> commentDTOS, Long articleId);

    /**
     * 填充CommentDTO下的发布评论者用户信息（如果是登录用户发布的话）
     * @param dto /
     */
    void setUserInfo(CommentDTO dto);

    void setUserInfo(Iterable<CommentDTO> dtos);

    List<CommentDTO> listByArticleId(Long articleId);

    List<CommentDTO> listPublicByArticleId(Long articleId);

    PageDTO<CommentDTO> queryBy(ArticleCommentQuery query, Pageable pageable);

    /**
     * 后台查询附带文章信息的评论
     * @param query /
     * @param pageable /
     * @return 附带文章信息的评论
     */
    PageDTO<CommentDTO> queryByWithArticle(ArticleCommentQuery query, Pageable pageable);

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
