package cn.neptu.neplog.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BlogConfigVO {

    private String blogName;

    private Long visitCount;

    private Date installTime;

    /**
     * icp备案
     */
    private String icp;

    private String globalCss;

    private String blogAvatar;

    private String authorName;
}
