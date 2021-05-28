package cn.neptu.neplog.utils;

import cn.neptu.neplog.config.common.SitemapConfig;
import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.model.support.SitemapEntry;
import cn.neptu.neplog.repository.ArticleRepository;
import cn.neptu.neplog.repository.CategoryRepository;
import cn.neptu.neplog.repository.TagRepository;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SitemapUtil implements InitializingBean {

    public static final String CHANGE_FREQ_ALWAYS = "always";
    public static final String CHANGE_FREQ_YEARLY = "yearly";
    public static final String CHANGE_FREQ_MONTHLY = "monthly";
    public static final String CHANGE_FREQ_WEEKLY = "weekly";
    public static final String CHANGE_FREQ_hourly = "hourly";
    public static final String CHANGE_FREQ_DAILY = "daily";
    public static final String CHANGE_FREQ_NEVER = "never";

    private final SitemapConfig sitemapConfig;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    private String articleBaseUrl;
    private String categoryBaseUrl;
    private String tagBaseUrl;
    private final SimpleDateFormat simpleDateFormat;

    public SitemapUtil(SitemapConfig sitemapConfig,
                       ArticleRepository articleRepository,
                       CategoryRepository categoryRepository,
                       TagRepository tagRepository) {
        this.sitemapConfig = sitemapConfig;
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;

        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void afterPropertiesSet(){
        // 所有loc加上BaseUrl
        for (SitemapEntry entry : sitemapConfig.getSitemapEntries().values()) {
            entry.setLoc(sitemapConfig.getBaseUrl()+"/"+entry.getLoc());
        }
        this.articleBaseUrl = sitemapConfig.getSitemapEntries().get("article").getLoc();
        this.categoryBaseUrl = sitemapConfig.getSitemapEntries().get("category").getLoc();
        this.tagBaseUrl = sitemapConfig.getSitemapEntries().get("tag").getLoc();
    }

    public List<Element> generateTagSitemap(){
        List<Tag> tags = tagRepository.findAll();

        return tags.stream().map(tag ->
                createUrlElement(
                        tagBaseUrl + "/" + tag.getId(),
                        simpleDateFormat.format(tag.getUpdateTime()),
                        null,
                        null)
        ).collect(Collectors.toList());
    }

    public List<Element> generateCategorySitemap(){
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(category ->
                createUrlElement(
                        categoryBaseUrl + "/" + category.getId(),
                        simpleDateFormat.format(category.getUpdateTime()),
                        null,
                        null)
        ).collect(Collectors.toList());
    }

    public List<Element> generateArticleSitemap(){
        ArticleQuery query = new ArticleQuery();
        query.setStatus(ArticleConstant.STATUS_PUBLISHED);
        query.setViewPermission(ArticleConstant.VIEW_PERMISSION_ANYBODY);
        query.setDeleted(false);
        List<Article> articles = articleRepository.findAll(query.toSpecification());

        return articles.stream().map(article ->
                createUrlElement(
                        articleBaseUrl + "/" + article.getId(),
                        simpleDateFormat.format(article.getUpdateTime()),
                        null,
                        null)
        ).collect(Collectors.toList());
    }

    public void generateSitemap(){
        Element urlset = DocumentHelper.createElement("urlset");
        urlset.addAttribute("xmlns","http://www.sitemaps.org/schemas/sitemap/0.9");

        // 页面URL
        Date now = new Date();
        String nowStr = simpleDateFormat.format(now);
        for(SitemapEntry entry: sitemapConfig.getSitemapEntries().values()){
            entry.setLastMod(nowStr);
            urlset.add(createUrlElement(entry));
        }

        // 文章，分类，标签URL
        List<Element> articles = generateArticleSitemap();
        List<Element> categories = generateCategorySitemap();
        List<Element> tags = generateTagSitemap();
        articles.forEach(urlset::add);
        categories.forEach(urlset::add);
        tags.forEach(urlset::add);

        // 输出
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        File sitemap = new File(sitemapConfig.getTargetFolder() + "/sitemap.xml");
        try {
            XMLWriter writer = new XMLWriter(new FileOutputStream(sitemap), format);
            writer.write(urlset);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Element createUrlElement(SitemapEntry entry){
        return createUrlElement(entry.getLoc(), entry.getLastMod(), entry.getChangeFreq(), entry.getPriority());
    }

    private Element createUrlElement(String loc, String lastmod, String changefreq, String priority){
        Element url = DocumentHelper.createElement("url");
        url.add(createElement("loc", loc));
        url.add(createElement("lastmod", lastmod));
        if(StringUtils.hasText(changefreq)){
            url.add(createElement("changefreq", changefreq));
        }
        if(StringUtils.hasText(priority)){
            url.add(createElement("priority", priority));
        }
        return url;
    }

    private Element createElement(String tag, String text){
        Element ele = DocumentHelper.createElement(tag);
        ele.setText(text);
        return ele;
    }
}
