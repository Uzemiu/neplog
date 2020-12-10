package cn.neptu.neplog.config;

import cn.neptu.neplog.model.option.UploadFileOption;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "neplog.file")
public class UploadFileConfiguration {

    private Map<String, UploadFileOption> options;

    public UploadFileOption get(String name){
        return options.get(name);
    }
}
