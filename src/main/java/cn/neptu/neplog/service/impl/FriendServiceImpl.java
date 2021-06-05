package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.dto.FriendDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.entity.Friend;
import cn.neptu.neplog.model.query.FriendQuery;
import cn.neptu.neplog.repository.FriendRepository;
import cn.neptu.neplog.service.FriendService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.FriendMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("linkService")
public class FriendServiceImpl extends AbstractCrudService<Friend, Long> implements FriendService {

    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;

    protected FriendServiceImpl(FriendRepository repository,
                                FriendMapper friendMapper) {
        super(repository);
        this.friendRepository = repository;
        this.friendMapper = friendMapper;
    }

    @Override
    public Friend update(FriendDTO friend) {
        Assert.notNull(friend, "Friend must not be null");
        return friendRepository.save(friendMapper.toEntity(friend));
    }

    @Override
    public Friend create(FriendDTO friend) {
        Assert.notNull(friend, "Friend must not be null");
        return friendRepository.save(friendMapper.toEntity(friend));
    }

    @Override
    public PageDTO<FriendDTO> queryBy(FriendQuery query, Pageable pageable) {
        Page<Friend> friends = friendRepository.findAll(query.toSpecification(),pageable);
        return new PageDTO<>(friends.map(friendMapper::toDto));
    }

    @Override
    public void updateStatusByIdIn(Collection<Long> ids, Integer status) {
        Assert.notNull(ids, "Ids must not be null");
        Assert.notNull(status, "Status must not be null");
        friendRepository.updateStatusByIdIn(ids, status);
    }

    @Override
    public List<FriendDTO> listFriendView() {
        return friendMapper.toDto(friendRepository.findByStatus(STATUS_PUBLIC));
    }

    @Override
    public Map<String, Long> countByLabel() {
        Map<String, Long> res = new HashMap<>();
        res.put("pending",friendRepository.countByStatus(STATUS_PENDING));
        res.put("public",friendRepository.countByStatus(STATUS_PUBLIC));
        return res;
    }

}
