package cn.neptu.neplog.event.listener;

import cn.neptu.neplog.event.ArticleViewEvent;
import cn.neptu.neplog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ArticleViewListener extends AbstractVisitEventListener{

    public static final String VISIT_NAME = "ArticleView";

    @Autowired
    public ArticleViewListener(ArticleService articleService) {
        super(articleService);
    }

    @Async
    @EventListener
    public void onPostViewEvent(ArticleViewEvent event){
        onApplicationEvent(event);
    }

    @Override
    public String getVisitName() {
        return VISIT_NAME;
    }
}
