package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.dto.FriendDTO;
import cn.neptu.neplog.model.entity.Friend;
import cn.neptu.neplog.model.params.query.FriendQuery;
import cn.neptu.neplog.repository.FriendRepository;
import cn.neptu.neplog.service.FriendService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.FriendMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("linkService")
public class FriendServiceImpl extends AbstractCrudService<Friend, Integer> implements FriendService {

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
    public List<FriendDTO> queryBy(FriendQuery query, Pageable pageable) {
        return friendMapper.toDto(friendRepository.findAll(query.toSpecification(),pageable).toList());
    }

    @Override
    public List<FriendDTO> listFriendView() {
        return friendMapper.toDto(friendRepository.findByStatus(1));
    }

    @Override
    public Map<String, Long> countByLabel() {
        Map<String, Long> res = new HashMap<>();
        res.put("pending",friendRepository.countByStatus(0));
        res.put("public",friendRepository.countByStatus(1));
        return res;
    }

}
