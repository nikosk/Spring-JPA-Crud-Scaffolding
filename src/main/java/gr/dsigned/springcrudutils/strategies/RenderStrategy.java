package gr.dsigned.springcrudutils.strategies;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nk
 */
public interface RenderStrategy<T> {

    String render(T data);

    void setup(HttpServletResponse response);
}
