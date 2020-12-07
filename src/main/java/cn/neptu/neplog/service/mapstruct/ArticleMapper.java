package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper extends BaseMapper<ArticleDTO, Article>{
}
