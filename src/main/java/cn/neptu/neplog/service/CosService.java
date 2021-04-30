package cn.neptu.neplog.service;

import java.util.Map;

public interface CosService extends FileService{

    /**
     * 对云存储进行身份验证，对桶/地区有效性进行验证
     * @param properties COS配置
     * @return 是否通过验证
     */
    boolean validate(Map<String, String> properties);

    Object listBuckets(Map<String, String> properties);


}
