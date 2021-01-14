package cn.neptu.neplog.model.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static cn.neptu.neplog.constant.CosPropertyConstant.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TencentCosProperty implements PropertyBean {

    private String secretId;

    private String secretKey;

    private String region;

    private String bucketName;

    public TencentCosProperty(Map<String, String> properties){
        secretId = properties.getOrDefault(QC_SECRET_ID, "");
        secretKey = properties.getOrDefault(QC_SECRET_KEY, "");
        region = properties.getOrDefault(QC_REGION, "");
        bucketName = properties.getOrDefault(QC_BUCKET_NAME, "");
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
