package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.CategoryDTO;
import cn.neptu.neplog.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<CategoryDTO, Category>{
}
