package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper extends BaseMapper<ArticleDTO, Article>{

    @Override
    @Mapping(target = "htmlContent", source = "htmlContent", ignore = true)
    @Mapping(target = "content", source = "content", ignore = true)
    ArticleDTO toDto(Article entity);
}
