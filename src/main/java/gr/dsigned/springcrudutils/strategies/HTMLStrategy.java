package gr.dsigned.springcrudutils.strategies;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.util.HtmlUtils;

/**
 *
 * @author nk
 */
public class HTMLStrategy<T> extends XMLStrategy<T> {

    @Override
    public String render(T data) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='utf-8'/><title>");
        sb.append(data.getClass().getName());
        sb.append("</title></head><body>");
        sb.append("<pre>");
        sb.append(HtmlUtils.htmlEscape(marshall(data)));
        sb.append("</pre>");
        sb.append("</body></html>");
        return sb.toString();
    }

    @Override
    public void setup(HttpServletResponse response) {
        response.setContentType(ContentType.TEXT_HTML.toString());
    }
}
