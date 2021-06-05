package cn.neptu.neplog.config.common;

import cn.neptu.neplog.utils.SitemapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeoConfig {

    private final SitemapConfig sitemapConfig;

    private final SitemapUtil sitemapUtil;

    /**
     * 每天凌晨两点生成sitemap
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void saveSitemap(){
        if(sitemapConfig.isEnabled()){
            sitemapUtil.generateSitemap();
        }
    }
}
