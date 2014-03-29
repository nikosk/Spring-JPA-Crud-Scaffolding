package gr.dsigned.repository.filters;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: nk Date: 2/15/14 Time: 10:30 PM
 */
public interface Filter<E> {

    public Filter<E> orderBy(String expression, Boolean asc);

    public List<E> list();


    public List<E> list(int maxNo, int offset);


    public Long count();

    public void setUp(FilterParameters<E> parameters);

}
