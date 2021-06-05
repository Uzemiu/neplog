package cn.neptu.neplog.service.base;

import cn.neptu.neplog.model.entity.BaseLike;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.repository.BaseLikeRepository;
import cn.neptu.neplog.utils.RequestUtil;
import cn.neptu.neplog.utils.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractLikeService<LIKE extends BaseLike>
        extends AbstractCrudService<LIKE, Long>
        implements BaseLikeService<LIKE>{

    private final BaseLikeRepository<LIKE> likeRepository;

    protected AbstractLikeService(BaseLikeRepository<LIKE> likeRepository) {
        super(likeRepository);
        this.likeRepository = likeRepository;
    }

    @Override
    public LIKE getLikeByTargetIdAndIdentity(Long targetId, HttpServletRequest request) {
        return likeRepository.findByTargetIdAndIdentity(targetId, getIdentity(request));
    }

    @Override
    public int updateLike(LIKE like, HttpServletRequest request) {
        like.setIdentity(getIdentity(request));
        LIKE prev = likeRepository.findByTargetIdAndIdentity(like.getTargetId(), like.getIdentity());
        int opinionScoreChange = like.getOpinion();
        if(prev != null){
            opinionScoreChange -= prev.getOpinion();
            like.setId(prev.getId());
            prev.setOpinion(like.getOpinion());
        }
        likeRepository.save(like);
        return opinionScoreChange;
    }

    private String getIdentity(HttpServletRequest request){
        User current = SecurityUtil.getCurrentUser();
        return current == null ? RequestUtil.getIp(request) : current.getId();
    }
}
