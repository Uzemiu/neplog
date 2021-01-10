package cn.neptu.neplog.model.property;

import lombok.Data;

@Data
public class BlogProperty {

    private String blogName;

    private Long visitCount;

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
     * icp备案号
     */
    private String icp;

    private String globalCSS;

    private String blogAvatar;

    private String authorName;
}
