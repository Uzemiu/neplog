package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.model.dto.TagDTO;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.repository.TagRepository;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.TagService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.service.mapstruct.TagMapper;
import cn.neptu.neplog.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static cn.neptu.neplog.constant.ArticleConstant.STATUS_PUBLISHED;

@Service("tagService")
public class TagServiceImpl extends AbstractCrudService<Tag, Long> implements TagService {

    private final TagRepository tagRepository;
    private final ArticleService articleService;
    private final TagMapper tagMapper;

    protected TagServiceImpl(TagRepository tagRepository,
                             ArticleService articleService,
                             TagMapper tagMapper) {
        super(tagRepository);
        this.tagRepository = tagRepository;
        this.articleService = articleService;
        this.tagMapper = tagMapper;
    }

    @Override
    public Set<Tag> findTagIn(Collection<String> tags) {
        return tagRepository.findByTagIn(tags);
    }

    @Override
    public List<TagDTO> listAllDTO() {
        List<TagDTO> tags = tagMapper.toDto(super.listAll());

        if(SecurityUtil.isOwner()){
            for(TagDTO t : tags){
                t.setArticleCount(tagRepository.getArticleCount(t.getId()));
            }
        } else {
            for(TagDTO t: tags){
                t.setArticleCount(articleService.countInvisibleArticlesByTag(t.getId()));
            }
        }
        return tags;
    }
}
