package gr.dsigned.springcrudutils.strategies;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

/**
 * @author nk
 */
public class JsonStrategy<T> implements RenderStrategy<T> {

    protected ObjectMapper mapper;

    public JsonStrategy() {
        this.mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String render(T data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setup(HttpServletResponse response) {
        response.setContentType(ContentType.JSON.toString());
    }
}
