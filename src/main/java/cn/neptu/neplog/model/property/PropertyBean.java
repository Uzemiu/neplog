package cn.neptu.neplog.model.property;

import java.util.Map;

public interface PropertyBean {

    PropertyBean fromMap(Map<String, String> map);

    Map<String, String> asMap();
}
