package cn.neptu.neplog.event.listener;

import cn.neptu.neplog.event.AbstractVisitEvent;
import cn.neptu.neplog.service.VisitService;
import cn.neptu.neplog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public abstract class AbstractVisitEventListener {

    private final VisitService visitService;
    private final ScheduledExecutorService executor;
    @Resource
    private RedisUtil redisUtil;

    public AbstractVisitEventListener(VisitService visitService) {
        this.visitService = visitService;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new VisitTask(),1,1, TimeUnit.HOURS);
    }

    public abstract String getVisitName();

    /**
     * Listening {@link ContextClosedEvent}to execute transaction
     * before context destroyed instead of annotated with {@link PreDestroy}
     */
    @EventListener
    public void onContextClosedEvent(ContextClosedEvent event) {
        flushAndShutdown();
    }

    public void onApplicationEvent(AbstractVisitEvent event) {
        String key = getVisitName() + event.getId();
        if(!redisUtil.sHasKey(key,event.getId())){
            redisUtil.sSet(key, event.getId());
            redisUtil.hincr(getVisitName(),event.getId(),1);
        }
    }

    protected void flush(){
        Map<Object,Object> s = redisUtil.hmget(getVisitName());
        s.forEach((key,value) -> {
            redisUtil.hset(getVisitName(), key.toString(),0);
            visitService.increaseVisit(key.toString(),(Integer) value);
        });
    }

    protected void flushAndReset(){
        flush();
        redisUtil.del(getVisitName());
    }

    protected void flushAndShutdown(){
        executor.shutdown();
        flush();
    }

    private class VisitTask implements Runnable{

        int count = 0;

        @Override
        public void run() {
            // flush visits every 1h
            // reset ip every 24h
            if((count = (count + 1) % 24) == 0){
                flushAndReset();
            } else {
                flush();
            }
        }
    }
}
