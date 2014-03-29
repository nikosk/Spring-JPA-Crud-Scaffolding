package gr.dsigned.web.rest.api;

import com.codahale.metrics.annotation.Timed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import gr.dsigned.domain.Identifiable;
import gr.dsigned.repository.filters.Filter;
import gr.dsigned.repository.filters.FilterParameters;
import gr.dsigned.web.events.ResourceCreatedEvent;
import gr.dsigned.web.events.ResourceDeletedEvent;
import gr.dsigned.web.events.ResourceUpdatedEvent;
import gr.dsigned.web.events.RestListPaginationEvent;
import gr.dsigned.web.exceptions.BindingException;


/**
 * Created by IntelliJ IDEA. User: nk Date: 2/12/14 Time: 8:15 AM
 */
public abstract class BaseRestController<K extends Serializable, E, F extends Filter<E>, P extends FilterParameters<E>> {


    @Autowired
    protected JpaRepository<E, K> dao;

    @Autowired
    protected Provider<F> filterProvider;

    @Autowired
    protected ApplicationEventPublisher publisher;

    protected F get() {
        return filterProvider.get();
    }

    @Timed
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<E> list(
        @Valid P parameters,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "page", required = false) Integer page, HttpServletRequest request, HttpServletResponse response
    ) {
        Filter<E> filter = get();
        filter.setUp(parameters);
        final List<E> list;
        if (size != null) {
            list = filter.list(size, page == null ? 0 : page * size);
            publisher.publishEvent(new RestListPaginationEvent(this, page, size, filter.count(), request, response));
        } else {
            list = filter.list();
        }
        return list;
    }

    @Timed
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public E show(@PathVariable("id") K id) throws Exception {
        return dao.findOne(id);
    }

    @Timed
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public E create(@Valid @RequestBody E entity, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new BindingException("Invalid request parameters", bindingResult.getAllErrors());
        } else {
            entity = dao.saveAndFlush(entity);
        }
        publisher.publishEvent(new ResourceCreatedEvent(this, (Identifiable) entity, request, response));
        return entity;
    }

    @Timed
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(
        @PathVariable("id") K id, @Valid @RequestBody E entity, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            throw new BindingException("Invalid request parameters", bindingResult.getAllErrors());
        } else {
            entity = dao.saveAndFlush(entity);
        }
        publisher.publishEvent(new ResourceUpdatedEvent(this, (Identifiable) entity, request, response));
    }

    @Timed
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") K id, HttpServletRequest request, HttpServletResponse response) {
        dao.delete(id);
        publisher.publishEvent(new ResourceDeletedEvent(this, null, request, response));
    }

}
