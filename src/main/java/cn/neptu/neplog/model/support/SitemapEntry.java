package cn.neptu.neplog.model.support;

import cn.neptu.neplog.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SitemapEntry {

    private String loc;

    private String lastMod;

    private String changeFreq;

    private String priority;

    public void setLoc(String loc) {
        this.loc = StringUtil.trim(loc, '/');
    }
}
