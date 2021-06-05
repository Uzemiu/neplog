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
