package cn.neptu.neplog.event.listener;

import cn.neptu.neplog.event.BlogVisitEvent;
import cn.neptu.neplog.service.PropertyService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BlogVisitListener extends AbstractVisitEventListener{

    public static final String VISIT_NAME  = "BlogVisit";

    public BlogVisitListener(PropertyService configService) {
        super(configService);
    }

    @EventListener
    public void onBlogVisitEvent(BlogVisitEvent event){
        onApplicationEvent(event);
    }

    @Override
    public String getVisitName() {
        return VISIT_NAME;
    }
}
