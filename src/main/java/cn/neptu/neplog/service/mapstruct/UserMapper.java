package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.UserDTO;
import cn.neptu.neplog.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserDTO, User>{

}
