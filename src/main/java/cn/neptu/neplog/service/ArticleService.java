package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.entity.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    Article save(ArticleDTO article);

    ArticleDTO findById(Integer id);
}
