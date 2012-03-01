package gr.dsigned.springcrudutils.strategies;

import com.google.common.collect.Lists;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

/**
 *
 * @author nk
 */
public class XMLStrategy<T> implements RenderStrategy<T> {

    private List<Class> registeredTypes = Lists.<Class>newArrayList(CollectionWrapper.class, MapEntryWrapper.class, ArrayList.class, HashMap.class);

    @Override
    public String render(T data) {
        return marshall(data);
    }

    protected String marshall(T data) {
        if (data == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        try {
            registeredTypes.add(data.getClass());
            JAXBContext context = JAXBContext.newInstance(registeredTypes.toArray(new Class[0]));            
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(new JAXBElement(new QName(data.getClass().getSimpleName()), data.getClass(), normalize(data)), sw);
        } catch (JAXBException ex) {
            throw new RuntimeException("Xml render exception with: " + data.toString(), ex);
        }
        return sw.toString();
    }

    @Override
    public void setup(HttpServletResponse response) {
        response.setContentType(ContentType.APPLICATION_XML.toString());
    }

    private Object normalize(Object o) {
        if (o == null) {
            return null;
        }
        Object toReturn = null;
        if (Collection.class.isAssignableFrom(o.getClass())) {
            CollectionWrapper wrapper = new CollectionWrapper();
            Iterator i = ((Collection) o).iterator();
            while (i.hasNext()) {
                Object value = i.next();
                wrapper.add(normalize(value));
            }
            toReturn = wrapper;
        } else if (Map.class.isAssignableFrom(o.getClass())) {
            CollectionWrapper wrapper = new CollectionWrapper();
            Iterator i = ((Map) o).entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                Object key = normalize(entry.getKey());
                Object value = normalize(entry.getValue());
                MapEntryWrapper entryWrapper = new MapEntryWrapper(key, value);
                wrapper.add(entryWrapper);
            }
            toReturn = wrapper;
        } else {
            toReturn = o;
        }
        return toReturn;
    }
}

@XmlRootElement(name = "entry")
class MapEntryWrapper {

    @XmlElement
    private Object key;
    @XmlElement
    private Object value;

    public MapEntryWrapper() {
    }

    public MapEntryWrapper(Object key, Object value) {
        this.key = key;
        this.value = value;
    }
}

@XmlRootElement(name = "collection")
class CollectionWrapper {

    @XmlElementWrapper(name = "collection")
    private ArrayList items = new ArrayList();

    public void add(Object toAdd) {
        items.add(toAdd);
    }
}