package cn.neptu.neplog.model.config;

import cn.neptu.neplog.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class BlogConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "blog_name")
    @ColumnDefault("'neplog'")
    private String blogName;

    @Column(name = "visit_count")
    @ColumnDefault("0")
    private Long visitCount;

    @Column(name = "install_time")
    private Date installTime;

    @Column(name = "friend_page_cover")
    @ColumnDefault("''")
    private String friendPageCover;

    @Column(name = "friend_page_title")
    @ColumnDefault("''")
    private String friendPageTitle;

    /**
     * 数字表示首页显示glides数量
     * 链接表示显示单张图片
     */
    @Column(name = "home_page_cover")
    @ColumnDefault("'3'")
    private String homePageCover;

    @Column(name = "home_page_title")
    @ColumnDefault("''")
    private String homePageTitle;

    /**
     * 首页显示文章类型（最近更新/最多阅读/随机）
     */
    @Column(name = "home_page_article")
    @ColumnDefault("''")
    private String homePageArticle;

    /**
     * icp备案
     */
    @Column(name = "icp")
    @ColumnDefault("''")
    private String icp;

    @Column(name = "global_css")
    @ColumnDefault("''")
    private String globalCss;

    //-----------

    @Column(name = "default_file_service")
    @ColumnDefault("'default'")
    private String defaultFileService;

    @Column(name = "available_file_service")
    @ColumnDefault("''")
    private String availableFileService;



    @Override
    protected void prePersist() {
        super.prePersist();
        if(blogName == null){
            blogName = "Neplog";
        }
        if(visitCount == null){
            visitCount = 0L;
        }
        if(installTime == null){
            installTime = new Date();
        }
        if(friendPageCover == null){
            friendPageCover = "";
        }
        if(friendPageTitle == null){
            friendPageTitle = "";
        }
        if(homePageCover == null){
            homePageCover = "3";
        }
        if(homePageArticle == null){
            homePageCover = "";
        }
        if(homePageTitle == null){
            homePageTitle = "";
        }
        if(icp == null){
            icp = "";
        }
        if(globalCss == null){
            globalCss = "";
        }
        if(defaultFileService == null){
            defaultFileService = "default";
        }
        if(availableFileService == null){
            defaultFileService = "";
        }
    }
}
