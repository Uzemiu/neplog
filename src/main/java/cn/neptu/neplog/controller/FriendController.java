package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.dto.FriendDTO;
import cn.neptu.neplog.model.query.FriendQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/view")
    @AnonymousAccess
    public BaseResponse<?> listFriendsView(){
        return BaseResponse.ok("ok",friendService.listFriendView());
    }

    @GetMapping
    public BaseResponse<?> queryBy(@Validated FriendQuery query,
                                   @PageableDefault(sort = {"updateTime"},size = 9527,
                                           direction = Sort.Direction.DESC) Pageable pageable){
        if(query.getStatus() == null){
            query.setStatus(1);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("friends",friendService.queryBy(query,pageable));
        res.put("count",friendService.countByLabel());
        return BaseResponse.ok("ok",res);
    }

    @PostMapping
    @AnonymousAccess
    public BaseResponse<?> createFriend(@RequestBody @Validated FriendDTO param){
        return BaseResponse.ok("ok",friendService.create(param));
    }

    @PutMapping
    public BaseResponse<?> updateFriend(@RequestBody @Validated FriendDTO param){
        friendService.update(param);
        return BaseResponse.ok();
    }

    @DeleteMapping
    public BaseResponse<?> deleteFriend(@RequestBody List<Long> ids){
        long count = friendService.deleteByIdIn(ids);
        return BaseResponse.ok("ok",count);
    }
}
