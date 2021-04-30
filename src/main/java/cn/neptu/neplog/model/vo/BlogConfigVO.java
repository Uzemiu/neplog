package cn.neptu.neplog.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BlogConfigVO {

    private String blogName;

    private Long visitCount;

    private Date installTime;

    private String friendPageCover;

    private String friendPageTitle;

    /**
     * 数字表示首页显示glides数量
     * 链接表示显示单张图片
     */
    private String homePageCover;

    private String homePageTitle;

    /**
     * icp备案
     */
    private String icp;

    private String globalCss;

    private String blogAvatar;

    private String authorName;
}
