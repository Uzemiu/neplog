package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.entity.ArticleLike;
import cn.neptu.neplog.repository.ArticleLikeRepository;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.service.base.AbstractLikeService;
import cn.neptu.neplog.service.base.BaseLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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
        int scoreChange = super.updateLike(like, request);
        if(scoreChange != 0){
            articleRepository.updateLikes(like.getTargetId(), (long)scoreChange);
        }
        return scoreChange;
    }
}
