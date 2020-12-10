package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.repository.TagRepository;
import cn.neptu.neplog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("tagService")
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public List<Tag> saveAll(Iterable<Tag> tags){
        return tagRepository.saveAll(tags);
    }

    @Override
    public Set<Tag> findByArticleId(Integer articleId) {
        return tagRepository.findByArticleId(articleId);
    }

    @Override
    public Set<Tag> findTagIn(Collection<String> tags) {
        return tagRepository.findByTagIn(tags);
    }

    @Override
    public int deleteTagsNotIn(Integer articleId, Collection<String> tags) {
        Assert.notNull(articleId,"Article Id must not be null");
        return tagRepository.deleteByArticleIdAndTagNotIn(articleId,tags);
    }
}
