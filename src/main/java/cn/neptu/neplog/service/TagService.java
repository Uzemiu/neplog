package cn.neptu.neplog.service;

import cn.neptu.neplog.model.dto.TagDTO;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.service.base.CrudService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TagService extends CrudService<Tag, Long> {

    Set<Tag> findTagIn(Collection<String> tags);

    List<TagDTO> listAllDTO();
}
