package gr.dsigned.repository.filters;

/**
 * Created by IntelliJ IDEA. User: nk Date: 2/17/14 Time: 9:37 AM
 */
public class AbstractOrderableParameters<E> implements FilterParameters<E> {

    private String orderBy;

    @Override
    public String orderBy() {
        return orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
