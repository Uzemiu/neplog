package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.dto.TagDTO;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.repository.TagRepository;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.TagMapper;
import cn.neptu.neplog.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static cn.neptu.neplog.constant.ArticleConstant.*;

@Service("tagService")
public class TagServiceImpl extends AbstractCrudService<Tag, Long> implements TagService {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final TagMapper tagMapper;

    protected TagServiceImpl(TagRepository tagRepository,
                             ArticleRepository articleRepository, TagMapper tagMapper) {
        super(tagRepository);
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public Tag create(Tag tag) {
        tagRepository.findByTag(tag.getTag())
                .ifPresent(t -> {throw new BadRequestException(tag.getTag() + " 已经存在"); });
        return tagRepository.save(tag);
    }

    @Override
    public Tag update(Tag tag) {
        return create(tag);
    }

    @Override
    public Set<Tag> findTagIn(Collection<String> tags) {
        return tagRepository.findByTagIn(tags);
    }

    @Override
    public List<TagDTO> listAllDTO() {
        List<TagDTO> tags = tagMapper.toDto(listAll());

        if(SecurityUtil.isOwner()){
            for(TagDTO t : tags){
                t.setArticleCount(tagRepository.getArticleCount(t.getId()));
            }
        } else {
            for(TagDTO t: tags){
                long count = articleRepository.countByTags(
                        t.getId(), STATUS_PUBLISHED, VIEW_PERMISSION_ANYBODY, false);
                t.setArticleCount(count);
            }
        }
        return tags;
    }

    @Transactional
    @Override
    public Tag deleteById(Long id) {
        Tag tag = super.deleteById(id);
        tagRepository.unlinkTag(id);
        return tag;
    }

    @Override
    public long deleteByIdIn(Collection<Long> longs) {
        long count = 0L;
        for(Long id: longs){
            if(deleteById(id) != null){
                count++;
            }
        }
        return count;
    }
}
