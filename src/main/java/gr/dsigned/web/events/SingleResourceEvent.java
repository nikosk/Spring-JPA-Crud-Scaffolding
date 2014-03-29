package gr.dsigned.web.events;


import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gr.dsigned.domain.Identifiable;


/**
 * Created by IntelliJ IDEA. User: nk Date: 2/9/14 Time: 2:31 PM
 */
public abstract class SingleResourceEvent extends ApplicationEvent {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Identifiable entity;
    private final Class<?> controller;
    /**
     * Create a new ApplicationEvent.
     *
     * @param controller the component that published the event (never {@code null})
     */
    public SingleResourceEvent(Object controller, Identifiable entity, HttpServletRequest request, HttpServletResponse response) {
        super(controller);
        this.controller = controller.getClass();
        this.request = request;
        this.response = response;
        this.entity = entity;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Identifiable getEntity() {
        return entity;
    }

    public Class<?> getController() {
        return controller;
    }
}
