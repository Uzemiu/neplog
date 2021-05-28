package cn.neptu.neplog.service.base;

import cn.neptu.neplog.model.entity.BaseLike;
import cn.neptu.neplog.service.base.CrudService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface BaseLikeService<LIKE extends BaseLike> extends CrudService<LIKE, Long> {

    LIKE getLikeByTargetIdAndIdentity(Long targetId, HttpServletRequest request);

    int updateLike(LIKE like, HttpServletRequest request);
}
