package gr.dsigned.springcrudutils.strategies;

/**
 *
 * @author nk
 */
public class JsonpStrategy<T> extends JsonStrategy<T> {

    private String callback;

    public JsonpStrategy(String callback) {
        this.callback = callback;
    }

    @Override
    public String render(T data) {
        return callback + "(" + super.render(data) + ");";
    }
}
