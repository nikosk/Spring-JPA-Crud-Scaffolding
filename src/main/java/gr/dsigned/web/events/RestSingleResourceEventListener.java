package gr.dsigned.web.events;

import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA. User: nk Date: 2/9/14 Time: 2:33 PM
 */
@Component
public class RestSingleResourceEventListener implements ApplicationListener<SingleResourceEvent> {

    @Override
    public void onApplicationEvent(SingleResourceEvent event) {
        RequestMapping mapping = AnnotationUtils.findAnnotation(event.getController(), RequestMapping.class);
        if (mapping == null || mapping.value().length < 1) {
            throw new IllegalStateException("No mapping found on controller");
        }
        String path = mapping.value()[0];
        if (event instanceof ResourceCreatedEvent) {
            event.getResponse().setStatus(HttpStatus.CREATED.value());
            event.getResponse().addHeader("Location", path + "/" + event.getEntity().getId());
        } else if (event instanceof ResourceUpdatedEvent) {
            event.getResponse().setStatus(HttpStatus.ACCEPTED.value());
            event.getResponse().addHeader("Location", path + "/" + event.getEntity().getId());
        } else if (event instanceof ResourceDeletedEvent) {
            event.getResponse().setStatus(HttpStatus.ACCEPTED.value());
        }
    }
}

