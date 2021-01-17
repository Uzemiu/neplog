package cn.neptu.neplog.model.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static cn.neptu.neplog.constant.MailConstant.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailProperty implements PropertyBean{

    private String host;

    private String username;

    private String password;

    private Integer port;

    private String from;

    private String protocol;

    private String encoding;

    public MailProperty(Map<String, String> properties){
        host = properties.get(HOST);
        username = properties.get(USERNAME);
        password = properties.get(PASSWORD);
        try{
            port = Integer.valueOf(properties.getOrDefault(PORT,"-1"));
        } catch (Exception e){
            port = -1;
        }
        protocol = properties.get(PROTOCOL);
        from = properties.get(FROM);
        encoding = properties.getOrDefault(ENCODING, "utf-8");
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>(7);
        map.put(HOST, host);
        map.put(USERNAME, username);
        map.put(PASSWORD, password);
        map.put(PORT, port);
        map.put(PROTOCOL, protocol);
        map.put(FROM, from);
        map.put(ENCODING, encoding.toString());
        return map;
    }
}
