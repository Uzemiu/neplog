package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.entity.ArticleLike;
import cn.neptu.neplog.model.entity.BaseLike;
import cn.neptu.neplog.repository.ArticleLikeRepository;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.service.base.AbstractLikeService;
import cn.neptu.neplog.service.base.BaseLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@Service("articleLikeService")
public class ArticleLikeServiceImpl
        extends AbstractLikeService<ArticleLike>
        implements BaseLikeService<ArticleLike> {

    private final ArticleLikeRepository likeRepository;
    private final ArticleRepository articleRepository;

    public ArticleLikeServiceImpl(ArticleLikeRepository likeRepository,
                                  ArticleRepository articleRepository) {
        super(likeRepository);
        this.likeRepository = likeRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    @Override
    public int updateLike(ArticleLike like, HttpServletRequest request) {
        articleRepository.findById(like.getTargetId())
                .orElseThrow(() -> new BadRequestException("文章不存在"));
        int scoreChange = super.updateLike(like, request);
        if(scoreChange != 0){
            articleRepository.updateLikes(like.getTargetId(), (long)scoreChange);
        }
        return scoreChange;
    }

    @Transactional
    @Override
    public ArticleLike deleteById(Long aLong) {
        ArticleLike like = getNotNullById(aLong);
        articleRepository.updateLikes(like.getTargetId(), (long)-like.getOpinion());
        likeRepository.delete(like);
        return like;
    }

    @Transactional
    @Override
    public long deleteByIdIn(Collection<Long> longs) {
        long count = 0;
        for (Long aLong : longs) {
           count += deleteById(aLong) == null ? 0 : 1;
        }
        return count;
    }
}
