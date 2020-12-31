package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.service.base.CrudService;

import java.util.List;

public interface CommentService extends CrudService<ArticleComment, Integer> {

    int countByArticleId(Integer articleId);

    ArticleComment create(CommentDTO commentDTO);

    List<CommentDTO> listByArticleId(Integer articleId);

    /**
     * 构建评论树结构并注入父评论作者信息
     * @param allComments 文章下所有的评论
     * @return 顶层评论（直接回复文章的论文）
     */
    List<CommentDTO> buildSimpleCommentTree(List<CommentDTO> allComments);
}
