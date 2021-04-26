package cn.neptu.neplog.repository;

import cn.neptu.neplog.model.config.MailConfig;
import cn.neptu.neplog.model.support.BaseResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface MailConfigRepository extends BaseRepository<MailConfig, Long> {
}
