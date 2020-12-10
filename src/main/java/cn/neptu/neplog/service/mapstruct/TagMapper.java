package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.TagDTO;
import cn.neptu.neplog.model.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper extends BaseMapper<TagDTO, Tag>{
}
