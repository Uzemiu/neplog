package cn.neptu.neplog.service;

import cn.neptu.neplog.model.params.InstallParam;
import org.springframework.transaction.annotation.Transactional;

public interface InstallService {

    @Transactional
    void installBlog(InstallParam param);
}
