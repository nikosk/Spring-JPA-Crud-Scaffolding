package gr.dsigned.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA. User: nk Date: 2/9/14 Time: 12:02 PM
 */
@Component
public class ConfigurableObjectMapper extends MappingJackson2HttpMessageConverter {

    Logger logger = LoggerFactory.getLogger(ConfigurableObjectMapper.class);

    public ConfigurableObjectMapper() {
        super();
        ObjectMapper objectMapper = new ObjectMapper();
        final Hibernate4Module module = new Hibernate4Module();
        module.enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
        objectMapper.registerModule(module);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        logger.debug("Created ConfigurableObjectMapper");
        setObjectMapper(objectMapper);
    }
}
