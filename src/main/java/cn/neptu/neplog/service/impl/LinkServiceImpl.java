package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.entity.Link;
import cn.neptu.neplog.repository.BaseRepository;
import cn.neptu.neplog.repository.LinkRepository;
import cn.neptu.neplog.service.LinkService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service("linkService")
public class LinkServiceImpl extends AbstractCrudService<Link, Integer> implements LinkService {
    @Override
    public Link save(Link link) {
        return null;
    }

    protected LinkServiceImpl(LinkRepository repository) {
        super(repository);
    }


}
