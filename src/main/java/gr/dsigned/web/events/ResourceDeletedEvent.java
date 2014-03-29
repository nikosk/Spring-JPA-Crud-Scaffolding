package gr.dsigned.web.events;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gr.dsigned.domain.Identifiable;


/**
 * Created by IntelliJ IDEA. User: nk Date: 2/9/14 Time: 2:34 PM
 */
public class ResourceDeletedEvent extends SingleResourceEvent {

    public ResourceDeletedEvent(
        Object controller,
        Identifiable entity,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        super(controller, entity, request, response);
    }
}
