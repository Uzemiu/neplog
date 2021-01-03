package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.FriendDTO;
import cn.neptu.neplog.model.entity.Friend;
import cn.neptu.neplog.model.query.FriendQuery;
import cn.neptu.neplog.service.base.CrudService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface FriendService extends CrudService<Friend, Long> {

    Integer STATUS_PENDING = 0;

    Integer STATUS_PUBLIC = 1;

    Friend create(FriendDTO friend);

    Friend update(FriendDTO friend);

    List<FriendDTO> queryBy(FriendQuery query, Pageable pageable);

    List<FriendDTO> listFriendView();

    Map<String, Long> countByLabel();
}
