package gr.dsigned.repository.filters;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by IntelliJ IDEA. User: nk Date: 2/15/14 Time: 10:32 PM
 */
@SuppressWarnings("unchecked")
public abstract class AbstractFilter<E> implements Filter<E> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFilter.class);

    protected CriteriaBuilder cb;

    protected CriteriaQuery query;

    protected Root<E> root;

    protected Predicate whereClause;

    @PersistenceContext
    protected EntityManager entityManager;

    @PostConstruct
    public void init() {
        cb = entityManager.getCriteriaBuilder();
        query = cb.createQuery();
        whereClause = cb.equal(cb.literal(1), 1);
        root = query.from(getEntityClass());
        logger.trace("Created filter for {} {}", getEntityClass(), System.identityHashCode(this));
    }


    @Override
    public void setUp(FilterParameters<E> parameters) {
        BeanWrapper bean = new BeanWrapperImpl(parameters);
        final Field[] declaredFields = parameters.getClass().getDeclaredFields();
        for (Field pd : declaredFields) {
            final String name = pd.getName();
            final Object value = bean.getPropertyValue(name);
            if (!name.startsWith("orderBy")) {
                wherePropertyEquals(name, value);
            }
        }
        if (parameters.orderBy() != null) {
            String expression = parameters.orderBy();
            if (expression.endsWith("ASC")) {
                expression = expression.replace("ASC", "");
                this.orderBy(expression, true);
            } else {
                expression = expression.replace("DESC", "");
                this.orderBy(expression, false);
            }
        }
    }

    public AbstractFilter<E> wherePropertyEquals(String propertyName, Object value) {
        if (value != null) {
            Predicate p = cb.equal(root.get(propertyName), value);
            whereClause = cb.and(whereClause, p);
        }
        return this;
    }

    public AbstractFilter<E> orderBy(String expression, Boolean asc) {
        if (asc == null || asc) {
            query.orderBy(cb.asc(root.get(expression)));
        } else {
            query.orderBy(cb.desc(root.get(expression)));
        }
        return this;
    }

    private CriteriaQuery<E> getQuery() {
        query.select(root);
        query.where(whereClause);
        logger.trace("Returning select query {}", query.getRoots());
        return query;
    }


    private CriteriaQuery<Long> getQueryForCount() {
        query.select(cb.count(root));
        query.where(whereClause);
        logger.trace("Returning count query {}", query.getRoots());
        return query;
    }

    private Class getEntityClass() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass()
            .getGenericSuperclass();
        return (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Timed
    public List<E> list() {
        TypedQuery<E> query = entityManager.createQuery(getQuery());
        query.setHint("org.hibernate.cacheable", Boolean.TRUE);
        return query.getResultList();
    }

    @Timed
    public List<E> list(int maxNo, int offset) {
        TypedQuery<E> query = entityManager.createQuery(getQuery());
        query.setFirstResult(offset);
        query.setMaxResults(maxNo);
        query.setHint("org.hibernate.cacheable", Boolean.TRUE);
        return query.getResultList();
    }

    @Timed
    public Long count() {
        TypedQuery<Long> query = entityManager.createQuery(getQueryForCount());
        query.setHint("org.hibernate.cacheable", Boolean.TRUE);
        return query.getSingleResult();
    }
}
