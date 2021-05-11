package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.dto.TagDTO;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.repository.TagRepository;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("tagService")
public class TagServiceImpl extends AbstractCrudService<Tag, Long> implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    protected TagServiceImpl(TagRepository tagRepository,
                             TagMapper tagMapper) {
        super(tagRepository);
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public Set<Tag> findTagIn(Collection<String> tags) {
        return tagRepository.findByTagIn(tags);
    }

}
