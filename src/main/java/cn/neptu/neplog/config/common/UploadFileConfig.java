package cn.neptu.neplog.config.common;

import cn.neptu.neplog.model.support.UploadFileOption;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "neplog.file")
public class UploadFileConfig {

    private Map<String, UploadFileOption> options;

    public UploadFileOption get(String name){
        return options.get(name);
    }
}
