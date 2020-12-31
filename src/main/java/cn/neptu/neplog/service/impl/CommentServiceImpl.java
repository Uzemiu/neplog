package cn.neptu.neplog.service.impl;


import cn.neptu.neplog.model.dto.CommentAuthorDTO;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.repository.CommentRepository;
import cn.neptu.neplog.service.CommentService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.CommentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("commentServiceImpl")
public class CommentServiceImpl extends AbstractCrudService<ArticleComment, Integer> implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    protected CommentServiceImpl(CommentRepository repository,
                                 CommentMapper commentMapper) {
        super(repository);
        this.commentRepository = repository;
        this.commentMapper = commentMapper;
    }

    @Override
    public int countByArticleId(Integer articleId) {
        return commentRepository.countByArticleId(articleId);
    }

    @Override
    public List<CommentDTO> buildSimpleCommentTree(List<CommentDTO> allComments) {
        Map<Integer, CommentDTO> map = buildCommentMap(allComments);

        List<CommentDTO> topComments = new ArrayList<>();
        allComments.forEach(comment -> {
            if(comment.getFatherId() == null){
               topComments.add(comment);
            } else {
                CommentDTO father = map.get(comment.getFatherId());
                if(father != null) {
                    comment.setAuthor(new CommentAuthorDTO(father.getAvatar(),father.getNickname(),father.getLink()));
                    father.getChildren().add(comment);
                }
            }
        });
        return topComments;
    }


    public List<CommentDTO> flatMap(List<CommentDTO> commentTree){
        return flatMap(commentTree, 2);
    }

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

    private Map<Integer, CommentDTO> buildCommentMap(List<CommentDTO> commentDTOList){
        Map<Integer, CommentDTO> map = new HashMap<>(commentDTOList.size());
        commentDTOList.forEach(comment -> map.put(comment.getId(), comment));
        return map;
    }

    @Override
    public List<CommentDTO> listByArticleId(Integer articleId) {
        return commentMapper.toDto(commentRepository.findByArticleId(articleId));
    }

    @Override
    public ArticleComment create(CommentDTO commentDTO) {
        return commentRepository.save(commentMapper.toEntity(commentDTO));
    }

    @Transactional
    @Override
    public ArticleComment deleteById(Integer integer) {
        ArticleComment comment = super.deleteById(integer);
        Map<Integer, CommentDTO> map = buildCommentMap(listByArticleId(comment.getArticleId()));


        return comment;
    }
}
