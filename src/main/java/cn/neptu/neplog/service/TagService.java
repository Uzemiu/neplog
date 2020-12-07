package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TagService {

    List<Tag> saveAll(Iterable<Tag> tags);

    Set<Tag> findTagIn(Collection<String> tags);

    int deleteTagsNotIn(Integer articleId, Collection<String> tags);
}
