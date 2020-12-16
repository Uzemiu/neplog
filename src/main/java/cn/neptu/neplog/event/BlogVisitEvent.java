package cn.neptu.neplog.event;

public class BlogVisitEvent extends AbstractVisitEvent{

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source   the object on which the event initially occurred or with
     *                 which the event is associated (never {@code null})
     * @param id       the id of the target
     * @param sourceIp where the visit come from
     */
    public BlogVisitEvent(Object source, String id, String sourceIp) {
        super(source, id, sourceIp);
    }
}
