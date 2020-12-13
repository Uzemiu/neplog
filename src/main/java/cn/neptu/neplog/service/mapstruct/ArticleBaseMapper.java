package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleBaseMapper extends BaseMapper<ArticleBaseDTO, Article>{

    @Override
    @Mapping(target = "htmlContent",source = "htmlContent", ignore = true)
    ArticleBaseDTO toDto(Article entity);
}
