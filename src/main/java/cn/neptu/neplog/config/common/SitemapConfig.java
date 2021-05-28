package cn.neptu.neplog.config.common;

import cn.neptu.neplog.model.support.SitemapEntry;
import cn.neptu.neplog.utils.StringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static cn.neptu.neplog.utils.SitemapUtil.CHANGE_FREQ_ALWAYS;
import static cn.neptu.neplog.utils.SitemapUtil.CHANGE_FREQ_WEEKLY;

@Data
@Component
@ConfigurationProperties(prefix = "neplog.seo.sitemap")
public class SitemapConfig {

    private boolean enabled;

    private String baseUrl;

    private String targetFolder;

    private Map<String,SitemapEntry> sitemapEntries;

    public SitemapConfig() {
        // 设置默认值
        sitemapEntries = new HashMap<>();
        sitemapEntries.put("home", new SitemapEntry("","",CHANGE_FREQ_ALWAYS,"1"));
        sitemapEntries.put("about", new SitemapEntry("about","",CHANGE_FREQ_WEEKLY,"0.7"));
        sitemapEntries.put("friend", new SitemapEntry("friend","",CHANGE_FREQ_WEEKLY,"0.7"));
        sitemapEntries.put("article", new SitemapEntry("article","",CHANGE_FREQ_WEEKLY,"0.7"));
        sitemapEntries.put("tag", new SitemapEntry("tag","",CHANGE_FREQ_WEEKLY,"0.7"));
        sitemapEntries.put("category", new SitemapEntry("category","",CHANGE_FREQ_WEEKLY,"0.7"));
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = StringUtil.trim(baseUrl, '/');
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = StringUtils.trimTrailingCharacter(targetFolder, '/');
    }
}
