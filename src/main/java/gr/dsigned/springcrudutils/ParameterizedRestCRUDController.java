/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.springcrudutils;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gr.dsigned.springcrudutils.data.Dao;
import gr.dsigned.springcrudutils.data.annotations.FieldType;
import gr.dsigned.springcrudutils.strategies.RenderStrategy;
import gr.dsigned.springcrudutils.strategies.RenderStrategyFactory;
import gr.dsigned.springcrudutils.types.SystemEntity;
import gr.dsigned.springcrudutils.utils.HibernateDetachUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ParameterizedRestCRUDController<E extends SystemEntity> {

    private Dao dao;
    private Class<E> entityClass;

    public ParameterizedRestCRUDController(Class<E> entityClass, Dao dao) {
        this.dao = dao;
        this.entityClass = entityClass;
    }

    public void list(Integer page, Integer size, Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int sizeNo = size == null ? 50 : size.intValue();
        int pageNo = page == null ? 0 : (page.intValue());
        RenderStrategy renderStrategy = RenderStrategyFactory.getRenderStrategy(request);
        renderStrategy.setup(response);
        List<SystemEntity> results = dao.listAll(entityClass, sizeNo, pageNo);
        for (SystemEntity e : results) {
            HibernateDetachUtility.nullOutUninitializedFields(e, HibernateDetachUtility.SerializationType.SERIALIZATION);
        }
        model.put("items", results);
        model.put("count", dao.countAll(entityClass));
        response.getWriter().print(renderStrategy.render(model));
    }

    public void show(Long id, Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenderStrategy renderStrategy = RenderStrategyFactory.getRenderStrategy(request);
        renderStrategy.setup(response);
        SystemEntity entity = dao.findEntityById(entityClass, id);
        HibernateDetachUtility.nullOutUninitializedFields(entity, HibernateDetachUtility.SerializationType.SERIALIZATION);
        model.put("item", entity);
        response.getWriter().print(renderStrategy.render(model));
    }

    public void form(Map model, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        RenderStrategy renderStrategy = RenderStrategyFactory.getRenderStrategy(request);
        renderStrategy.setup(response);
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(entityClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null && pd.getReadMethod() != null) {
                Map field = Maps.newLinkedHashMap();
                Field f = entityClass.getDeclaredField(pd.getName());
                field.put("annotations", getAnnotations(f));
                field.put("type", pd.getPropertyType().getName());
                model.put(pd.getName(), field);
            }
        }
        response.getWriter().print(renderStrategy.render(model));
    }

    public void update(E entity, BindingResult bindingResult, Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenderStrategy renderStrategy = RenderStrategyFactory.getRenderStrategy(request);
        renderStrategy.setup(response);
        if (bindingResult.hasErrors()) {
            if (!model.containsKey("item")) {
                model.put("item", entity);
            }
            List<String> errorMessages = Lists.newArrayList();
            for (ObjectError err : bindingResult.getAllErrors()) {
                errorMessages.add(err.toString());
            }
            model.put("errors", errorMessages);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            dao.merge(entity);
            model.clear();
            HibernateDetachUtility.nullOutUninitializedFields(entity, HibernateDetachUtility.SerializationType.SERIALIZATION);
            model.put("item", entity);
        }
        response.getWriter().print(renderStrategy.render(model));
    }

    public void create(E entity, BindingResult bindingResult, Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenderStrategy renderStrategy = RenderStrategyFactory.getRenderStrategy(request);
        renderStrategy.setup(response);
        if (bindingResult.hasErrors()) {
            if (!model.containsKey("item")) {
                model.put("item", entity);
            }
            List<String> errorMessages = Lists.newArrayList();
            for (ObjectError err : bindingResult.getAllErrors()) {
                errorMessages.add(err.toString());
            }
            model.put("errors", errorMessages);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            dao.persist(entity);
            model.clear();
            HibernateDetachUtility.nullOutUninitializedFields(entity, HibernateDetachUtility.SerializationType.SERIALIZATION);
            model.put("item", entity);
        }
        response.getWriter().print(renderStrategy.render(model));
    }

    public void delete(Long id) {
        dao.removeById(id);
    }

    private Map<String, Map> getAnnotations(Field f) {
        Map<String, Map> annotations = Maps.newLinkedHashMap();
        for (Annotation a : f.getAnnotations()) {
            if (FieldType.class.isInstance(a)) {
                FieldType lt = (FieldType) a;
                Map<String, String> annotationData = Maps.newLinkedHashMap();
                annotationData.put("value", lt.value());
                annotationData.put("fieldLabel", lt.fieldLabel());
                annotationData.put("fieldValue", lt.fieldValue());
                annotations.put(a.annotationType().getSimpleName(), annotationData);
            }
        }
        return annotations.isEmpty() ? null : annotations;
    }
}
