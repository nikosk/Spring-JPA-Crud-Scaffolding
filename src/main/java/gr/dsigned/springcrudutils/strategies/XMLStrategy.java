package gr.dsigned.springcrudutils.strategies;

import com.thoughtworks.xstream.XStream;

import javax.servlet.http.HttpServletResponse;

/**
 * @author nk
 */
public class XMLStrategy<T> implements RenderStrategy<T> {

    private static final XStream xstream = new XStream();
    
    static{
        xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
    }

    @Override
    public String render(T data) {
        return marshall(data);
    }

    protected String marshall(T data) {
        if (data == null) {
            return null;
        }
        return xstream.toXML(data);
    }

    @Override
    public void setup(HttpServletResponse response) {
        response.setContentType(ContentType.APPLICATION_XML.toString());
    }
}