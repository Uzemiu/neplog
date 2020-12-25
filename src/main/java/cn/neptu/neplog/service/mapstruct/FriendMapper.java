package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.FriendDTO;
import cn.neptu.neplog.model.entity.Friend;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FriendMapper extends BaseMapper<FriendDTO, Friend>{
}
