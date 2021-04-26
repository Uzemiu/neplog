package cn.neptu.neplog.event.listener;

import cn.neptu.neplog.event.BlogVisitEvent;
import cn.neptu.neplog.service.BlogConfigService;
import cn.neptu.neplog.service.ConfigService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BlogVisitListener extends AbstractVisitEventListener{

    public static final String VISIT_NAME  = "BlogVisit";

    public BlogVisitListener(BlogConfigService blogConfigService) {
        super(blogConfigService);
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
