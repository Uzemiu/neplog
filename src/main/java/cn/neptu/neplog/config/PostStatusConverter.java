package cn.neptu.neplog.config;

import cn.neptu.neplog.model.enums.PostStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class PostStatusConverter implements Converter<Integer, PostStatus> {
    @Override
    public PostStatus convert(Integer source) {
        if(source < 0 || source > 1){
            throw new IllegalArgumentException("文章状态只能是草稿(0)或者发布(1)");
        }
        return PostStatus.values()[source];
    }
}
