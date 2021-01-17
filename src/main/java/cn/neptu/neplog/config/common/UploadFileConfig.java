package cn.neptu.neplog.config.common;

import cn.hutool.core.util.ArrayUtil;
import cn.neptu.neplog.model.support.UploadFileOption;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "neplog.file")
public class UploadFileConfig {

    private String root;

    private String virtual;

    private Map<String, UploadFileOption> options;

    public UploadFileOption get(String name){
        return options.get(name);
    }

    public void setRoot(String[] root){
        this.root = ArrayUtil.join(root, File.separator);
    }
}
