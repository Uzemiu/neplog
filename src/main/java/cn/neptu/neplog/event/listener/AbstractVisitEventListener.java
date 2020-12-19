package cn.neptu.neplog.event.listener;

import cn.neptu.neplog.event.AbstractVisitEvent;
import cn.neptu.neplog.service.VisitService;
import cn.neptu.neplog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
public abstract class AbstractVisitEventListener {

    private final VisitService visitService;
    @Resource
    private RedisUtil redisUtil;

    public AbstractVisitEventListener(VisitService visitService) {
        this.visitService = visitService;
    }

    public abstract String getVisitName();

    @Async
    public void onApplicationEvent(AbstractVisitEvent event) {
        String key = getVisitName() + ":" + event.getId();
        redisUtil.pfadd(key, event.getUserId());
    }

    /**
     * 异步刷新Redis缓存的访问量数据
     * 默认每天凌晨两点刷新
     */
    @Async
    @Scheduled(cron = "0 0 2 * * ?")
    protected void flush(){
        Set<String> keys = redisUtil.keys(getVisitName() + "*");
        for(String id : keys){
            long increment = redisUtil.pfcount(id);
            visitService.increaseVisit(id.substring(getVisitName().length() + 1),increment);
            log.info("Counting visit of {}: {}",id,increment);
            redisUtil.del(id);
        }
    }
}
