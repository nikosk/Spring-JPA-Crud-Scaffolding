package gr.dsigned.springcrudutils.data;

import gr.dsigned.springcrudutils.types.SystemEntity;

import java.util.List;

public abstract class Dao<K, E> {


    public abstract E persist(E entity);

    public abstract void remove(E entity);

    public abstract E findById(K id);

    public abstract void removeById(K id);

    public abstract E merge(E entity);

    public abstract Long countAll(Class<? extends SystemEntity> entity);

    public abstract List<E> listAll(Class<? extends SystemEntity> entity, int maxNo, int offset);

    public abstract List<E> listAll();

    public abstract SystemEntity findEntityById(Class<? extends SystemEntity> entity, K id);


}
