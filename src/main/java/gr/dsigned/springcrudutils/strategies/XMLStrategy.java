package gr.dsigned.springcrudutils.strategies;

import com.thoughtworks.xstream.XStream;

import javax.servlet.http.HttpServletResponse;

/**
 * @author nk
 */
public class XMLStrategy<T> implements RenderStrategy<T> {

    @Override
    public String render(T data) {
        return marshall(data);
    }

    protected String marshall(T data) {
        if (data == null) {
            return null;
        }
        return new XStream().toXML(data);
    }

    @Override
    public void setup(HttpServletResponse response) {
        response.setContentType(ContentType.APPLICATION_XML.toString());
    }
}