package gr.dsigned.springcrudutils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gr.dsigned.springcrudutils.data.Dao;
import gr.dsigned.springcrudutils.data.annotations.FieldType;
import gr.dsigned.springcrudutils.types.SystemEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: nk
 * Date: 2/16/13
 * Time: 3:30 PM
 */
public class ParameterizedRestCRUDControllerTest {

    Dao mockDao;
    HttpServletRequest request;
    HttpServletResponse response;
    ByteArrayOutputStream buf;
    PrintWriter writer;
    Map model;
    ParameterizedRestCRUDController controller;

    @Before
    public void setUp() throws Exception {
        mockDao = mock(Dao.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        buf = new ByteArrayOutputStream();
        writer = new PrintWriter(buf);
        model = Maps.newLinkedHashMap();
        when(response.getWriter()).thenReturn(writer);
        controller = new ParameterizedRestCRUDController(Person.class, mockDao);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testList() throws Exception {
        when(mockDao.listAll(Person.class, 10, 0)).thenReturn(Lists.newArrayList(new Person()));
        controller.list(0, 10, model, request, response);
        Assert.assertTrue(model.containsKey("items"));
        Assert.assertTrue(model.get("items") instanceof List);
        List items = (List) model.get("items");
        Assert.assertEquals(items.size(),1);
        Assert.assertTrue(model.containsKey("count"));
    }

    @Test
    public void testShow() throws Exception {
        when(mockDao.findEntityById(Person.class, 1l)).thenReturn(new Person());
        controller.show(1l,model, request, response);
        Assert.assertTrue(model.containsKey("item"));
        Assert.assertTrue(model.get("item") instanceof Person);
        Person person = (Person) model.get("item");
    }

    @Test
    public void testForm() throws Exception {
        controller.form(model,request,response);
        Assert.assertTrue(model.containsKey("id"));
        Assert.assertTrue(model.containsKey("name"));
        Assert.assertTrue(model.containsKey("lastName"));
        Assert.assertTrue(model.get("id") instanceof Map);
        Assert.assertTrue(model.get("name") instanceof Map);
        Assert.assertTrue(model.get("lastName") instanceof Map);
        Map id = (Map) model.get("id");
        Map name = (Map) model.get("name");
        Map lastName = (Map) model.get("lastName");
        Map annotations = (Map) name.get("annotations");
        Map fieldType = (Map) annotations.get("FieldType");
        Assert.assertTrue(id.get("type").toString().equals("java.lang.Long"));
        Assert.assertTrue(name.get("type").toString().equals("java.lang.String"));
        Assert.assertTrue(annotations.containsKey("FieldType"));
        Assert.assertTrue(fieldType.containsKey("value"));
        Assert.assertTrue(fieldType.containsKey("fieldLabel"));
        Assert.assertTrue(fieldType.get("fieldLabel").equals("Όνομα"));
        Assert.assertTrue(fieldType.containsKey("fieldValue"));
        Assert.assertTrue(lastName.get("type").toString().equals("java.lang.String"));
    }


    public static class Person implements SystemEntity {


        private Long id;
        @FieldType(value = "name",fieldLabel = "Όνομα")
        private String name;
        private String lastName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

}
