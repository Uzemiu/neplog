package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.dto.CategoryDTO;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",uses = {CategoryMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper extends BaseMapper<ArticleDTO, Article>{

    @Override
    @Mapping(target = "categoryId",source = "category",qualifiedByName = "toCategoryId")
    Article toEntity(ArticleDTO dto);

    @Named("toCategoryId")
    default Integer toCategoryId(CategoryDTO categoryDTO){
        return categoryDTO.getId();
    }
}
