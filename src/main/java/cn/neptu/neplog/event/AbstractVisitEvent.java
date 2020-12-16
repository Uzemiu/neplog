package cn.neptu.neplog.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

public abstract class AbstractVisitEvent extends ApplicationEvent {

    private final String id;

    private final String userId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param id the id of the target
     * @param userId where the visit comes from
     */
    public AbstractVisitEvent(Object source, String id, String userId) {
        super(source);
        Assert.notNull(id,"Id must not be null");
        this.id = id;
        this.userId = userId;
    }

    public String getId(){
        return id;
    }

    public String getUserId(){
        return userId;
    }
}
