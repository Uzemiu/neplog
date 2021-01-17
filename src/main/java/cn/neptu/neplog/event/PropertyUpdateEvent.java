package cn.neptu.neplog.event;

import cn.neptu.neplog.model.entity.Property;
import org.springframework.context.ApplicationEvent;

public class PropertyUpdateEvent extends ApplicationEvent {

    public PropertyUpdateEvent(Property property) {
        super(property);
    }
}
