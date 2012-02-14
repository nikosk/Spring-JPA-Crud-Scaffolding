/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.springcrudutils.strategies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nk
 */
public class TextPlainStrategy<T> implements RenderStrategy<T> {

    protected Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    public String render(T data) {        
        return g.toJson(data);
    }

    @Override
    public void setup(HttpServletResponse response) {
        response.setContentType(ContentType.TEXT_PLAIN.toString());
    }
}
