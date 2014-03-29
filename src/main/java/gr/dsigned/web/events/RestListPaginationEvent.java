package gr.dsigned.web.events;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA. User: nk Date: 2/9/14 Time: 2:35 PM
 */
public class RestListPaginationEvent extends ApplicationEvent {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Integer size;
    private final Integer page;
    private final Long count;
    private final Class<?> controller;

    public RestListPaginationEvent(Object controller, Integer page, Integer size, Long count, HttpServletRequest request, HttpServletResponse response) {
        super(controller);
        this.controller = controller.getClass();
        this.page = page;
        this.size = size;
        this.count = count;
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Integer getSize() {
        return size == null ? 10 : size;
    }

    public Integer getPage() {
        return page == null ? 0 : page;
    }

    public Long getCount() {
        return count == null ? 0 : count;
    }

    public Class<?> getControllerClass() {
        return controller;
    }
}
