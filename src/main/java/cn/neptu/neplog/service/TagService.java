package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TagService {

    List<Tag> findAll();

    List<Tag> saveAll(Iterable<Tag> tags);

    Set<Tag> findTagIn(Collection<String> tags);

    Set<Tag> findByArticleId(Long articleId);

    int deleteTagsNotIn(Long articleId, Collection<String> tags);
}
