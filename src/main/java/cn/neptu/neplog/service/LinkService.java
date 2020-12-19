package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Link;
import cn.neptu.neplog.service.base.CrudService;

public interface LinkService extends CrudService<Link, Integer> {

    Link save(Link link);

}
