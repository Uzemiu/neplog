package cn.neptu.neplog.model.property;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Map;

import static cn.neptu.neplog.constant.BlogPropertyConstant.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogProperty implements PropertyBean{

    private String blogName;

    private String visitCount;

    private String installTime;

    private String friendPageCover;

    private String friendPageTitle;

    /**
     * 数字表示首页显示glides数量
     * 链接表示显示单张图片
     */
    private String homePageCover;

    private String homePageTitle;

    /**
     * 首页显示文章类型（最近更新/最多阅读/随机）
     */
    private String homePageArticle;

    /**
     * icp备案
     */
    private String icp;

    private String globalCss;

    private String blogAvatar;

    private String authorName;

    public BlogProperty(Map<String, String> properties){
        blogName = properties.get(BLOG_NAME);
        visitCount = properties.getOrDefault(VISIT_COUNT, "0");
        installTime = properties.get(INSTALL_TIME);
        friendPageCover = properties.get(FRIEND_PAGE_COVER);
        friendPageTitle = properties.get(FRIEND_PAGE_TITLE);
        homePageCover = properties.get(HOME_PAGE_COVER);
        homePageTitle = properties.get(HOME_PAGE_TITLE);
        homePageArticle = properties.get(HOME_PAGE_ARTICLE);
        icp = properties.get(ICP);
        globalCss = properties.get(GLOBAL_CSS);
        // 其实博客头像和作者名并没有存入数据库
    }

    @Override
    public Map<String, Object> asMap() {
        return BeanUtil.beanToMap(this);
    }
}
