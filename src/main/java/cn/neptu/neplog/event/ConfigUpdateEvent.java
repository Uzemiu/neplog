package cn.neptu.neplog.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConfigUpdateEvent extends ApplicationEvent {

    private final String configName;

    private final Object newConfig;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ConfigUpdateEvent(Object source, String configName, Object newConfig) {
        super(source);
        this.configName = configName;
        this.newConfig = newConfig;
    }
}
