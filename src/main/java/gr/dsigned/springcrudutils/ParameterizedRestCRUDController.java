/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.springcrudutils;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gr.dsigned.springcrudutils.conversion.ConverterFactory;
import gr.dsigned.springcrudutils.conversion.EntityDTOConverter;
import gr.dsigned.springcrudutils.data.Dao;
import gr.dsigned.springcrudutils.data.annotations.ListType;
import gr.dsigned.springcrudutils.types.SystemEntity;
import gr.dsigned.springcrudutils.strategies.RenderStrategy;
import gr.dsigned.springcrudutils.strategies.RenderStrategyFactory;
import gr.dsigned.springcrudutils.types.SystemDTO;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;

public class ParameterizedRestCRUDController<E extends SystemEntity, D extends SystemDTO> {

    private Dao dao;
    private Class<E> entityClass;
    private Class<D> dtoClass;
    private ConverterFactory factory;
    
    public ParameterizedRestCRUDController(Class<E> entityClass, Class<D> dtoClass, Dao dao, ConverterFactory factory) {
        this.dao = dao;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
        this.factory = factory;
    }

    public void list(Integer page, Integer size, Map model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int sizeNo = size == null ? 50 : size.intValue();
        int pageNo = page == null ? 0 : (page.intValue());
        RenderStrategy renderStrategy = RenderStrategyFactory.getRenderStrategy(request);
        renderStrategy.setup(response);
        List<SystemEntity> results = dao.listAll(entityClass, sizeNo, pageNo);
        List<SystemDTO> toResponse = Lists.newArrayList();
        EntityDTOConverter converter = factory.getDTOConverter(entityClass);
        for (SystemEntity e : results) {
            toResponse.add(converter.convertToDTO(e));
        }
        model.put("items", toResponse);
        model.put("count", dao.countAll(entityClass));
        response.getWriter().print(renderStrategy.render(model));
    }

    public void show(Long id, Map model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        RenderStrategy renderStrategy = RenderStrategyFactory.getRenderStrategy(request);
        renderStrategy.setup(response);
        EntityDTOConverter converter = factory.getDTOConverter(entityClass);
        model.put("item", converter.convertToDTO(dao.findEntityById(entityClass, id)));
        response.getWriter().print(renderStrategy.render(model));
    }

    public void form(Map model, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        RenderStrategy renderStrategy = RenderStrategyFactory.getRenderStrategy(request);
        renderStrategy.setup(response);
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(dtoClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null && pd.getReadMethod() != null) {
                Map field = Maps.newHashMap();
                Field f = dtoClass.getDeclaredField(pd.getName());
                field.put("annotations", getAnnotations(f));
                field.put("type", pd.getPropertyType().getSimpleName());
                model.put(pd.getName(), field);
            }
        }
        response.getWriter().print(renderStrategy.render(model));
    }

    private Map<String, String> getAnnotations(Field f) {
        Map<String, String> annotations = Maps.newHashMap();
        for (Annotation a : f.getAnnotations()) {
            if (ListType.class.isInstance(a)) {
                ListType lt = (ListType) a;
                annotations.put(a.annotationType().getSimpleName(), lt.value().getSimpleName());
            }
        }
        return annotations.isEmpty() ? null : annotations;
    }
}
