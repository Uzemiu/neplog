package cn.neptu.neplog.service;

import cn.neptu.neplog.model.config.BlogConfig;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

public interface BlogConfigService extends ConfigService<BlogConfig, Long>, VisitService {

    String getDefaultFileService();
}
