package gr.dsigned.springcrudutils.data;

import gr.dsigned.springcrudutils.types.SystemEntity;
import java.util.List;

public interface Dao<K, E> {



    public E persist(E entity);

    public void remove(E entity);

    public E findById(K id);

    public void removeById(K id);

    public void merge(E entity);

    public Long countAll(Class<? extends SystemEntity> entity);

    public List<E> listAll(Class<? extends SystemEntity> entity, int maxNo, int offset);

    public List<E> listAll();

    public SystemEntity findEntityById(Class<? extends SystemEntity> entity, K id);


}
