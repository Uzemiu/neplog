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

    public static final String COUNT_PREFIX = "Count_";

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
        String countKey = COUNT_PREFIX + key;
        redisUtil.incr(countKey, redisUtil.sSet(key, event.getUserId()));
    }

    /**
     * 每天凌晨两点重置每个IP访问量统计
     */
    @Async
    @Scheduled(cron = "0 0 2 * * ? ")
    protected void reset(){
        Set<String> keys = redisUtil.keys(getVisitName() + "*");
        for(String id : keys){
            redisUtil.del(id);
        }
    }

    /**
     * 每两小时刷新一次访问量
     */
    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    protected void flush(){
        String countKey = COUNT_PREFIX + getVisitName();
        Set<String> keys = redisUtil.keys(countKey + "*");
        for(String id : keys){
            long count = redisUtil.incr(id, 0);
            visitService.increaseVisit(id.substring(countKey.length() + 1), count);
            log.info("Counting visits of {}: {}",id,count);
            redisUtil.del(id);
        }
    }
}
