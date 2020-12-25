package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.dto.CategoryDTO;
import cn.neptu.neplog.model.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",uses = {CategoryMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper extends BaseMapper<ArticleDTO, Article>{

}
