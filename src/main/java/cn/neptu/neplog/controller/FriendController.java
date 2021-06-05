package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.dto.FriendDTO;
import cn.neptu.neplog.model.entity.Friend;
import cn.neptu.neplog.model.query.FriendQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.FriendService;
import cn.neptu.neplog.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("查询通过的友链信息")
    @GetMapping
    @AnonymousAccess
    public BaseResponse<?> list(@PageableDefault(sort = "createTime", size = 9527,
            direction = Sort.Direction.DESC) Pageable pageable){
        FriendQuery query = new FriendQuery();
        query.setStatus(FriendService.STATUS_PUBLIC);
        return BaseResponse.ok("ok",friendService.queryBy(query,pageable));
    }

    @ApiOperation("后台查询友链信息")
    @GetMapping("/query")
    public BaseResponse<?> queryBy(FriendQuery query,
                                   @PageableDefault(sort = "createTime", size = 18,
                                           direction = Sort.Direction.DESC) Pageable pageable){
        return BaseResponse.ok("ok",friendService.queryBy(query,pageable));
    }

    @ApiOperation("查询通过/未通过友链数量")
    @GetMapping("/count")
    public BaseResponse<Map<?,?>> count(){
        return BaseResponse.ok("ok", friendService.countByLabel());
    }

    @ApiOperation("提交/创建友链")
    @PostMapping
    @AnonymousAccess
    public BaseResponse<Friend> createFriend(@RequestBody @Validated FriendDTO param){
        if(!SecurityUtil.isOwner()){
            // 普通用户默认设置状态待审核
            param.setStatus(FriendService.STATUS_PENDING);
        }
        return BaseResponse.ok("ok",friendService.create(param));
    }

    @ApiOperation("更新友链")
    @PutMapping
    public BaseResponse<?> updateFriend(@RequestBody @Validated FriendDTO param){
        friendService.update(param);
        return BaseResponse.ok("更新友链成功");
    }

    @ApiOperation("更新友链状态")
    @PutMapping("/status")
    public BaseResponse<?> updateStatus(@RequestBody List<Long> ids){
        friendService.updateStatusByIdIn(ids, FriendService.STATUS_PUBLIC);
        return BaseResponse.ok();
    }

    @DeleteMapping
    public BaseResponse<?> deleteFriend(@RequestBody List<Long> ids){
        friendService.deleteByIdIn(ids);
        return BaseResponse.ok("ok");
    }
}
