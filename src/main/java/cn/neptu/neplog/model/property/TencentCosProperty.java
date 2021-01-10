package cn.neptu.neplog.model.property;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

import static cn.neptu.neplog.constant.CosPropertyConstant.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class TencentCosProperty implements PropertyBean {

    private String secretId;

    private String secretKey;

    private String region;

    private String bucketName;

    @Override
    public TencentCosProperty fromMap(Map<String, String> map) {
        secretId = map.getOrDefault(QC_SECRET_ID, "");
        secretKey = map.getOrDefault(QC_SECRET_KEY, "");
        region = map.getOrDefault(QC_REGION, "");
        bucketName = map.getOrDefault(QC_BUCKET_NAME, "");
        return this;
    }

    @Override
    public Map<String, String> asMap() {
        Map<String ,String> map = new HashMap<>(4);
        map.put(QC_SECRET_ID, secretId);
        map.put(QC_SECRET_KEY, secretKey);
        map.put(QC_REGION, region);
        map.put(QC_BUCKET_NAME, bucketName);
        return map;
    }
}
