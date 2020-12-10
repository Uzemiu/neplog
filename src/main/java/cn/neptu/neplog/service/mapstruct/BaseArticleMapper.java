package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.ArticleBaseDTO;
import cn.neptu.neplog.model.entity.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseArticleMapper extends BaseMapper<ArticleBaseDTO, Article>{
}
