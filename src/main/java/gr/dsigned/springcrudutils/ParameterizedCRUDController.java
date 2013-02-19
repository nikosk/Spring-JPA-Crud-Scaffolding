/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.springcrudutils;

import gr.dsigned.springcrudutils.data.Dao;
import gr.dsigned.springcrudutils.types.SystemEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public class ParameterizedCRUDController<E extends SystemEntity> {

    private Dao dao;
    private Class<E> entityClass;
    private String urlPrefix = "";

    public ParameterizedCRUDController(Class<E> type, Dao dao) {
        this.dao = dao;
        this.entityClass = type;
    }

    public ParameterizedCRUDController(Class<E> type, Dao dao, String urlPrefix) {
        this.dao = dao;
        this.entityClass = type;
        this.urlPrefix = urlPrefix;
    }

    public String list(Integer page, Integer size, Model model, HttpServletRequest request) {
        int sizeNo = size == null ? 50 : size.intValue();
        int pageNo = page == null ? 0 : (page.intValue());
        int offset = pageNo * sizeNo;
        Long itemNo = dao.countAll(entityClass);
        Pager pager = new Pager(request.getRequestURI(), itemNo.intValue(), pageNo, sizeNo);
        model.addAttribute("itemNo", itemNo);
        model.addAttribute("sizeNo", sizeNo);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("items", dao.listAll(entityClass, sizeNo, offset));
        model.addAttribute("pager", pager);
        return urlPrefix + entityClass.getSimpleName().toLowerCase() + "/list";
    }

    public String show(Long id, Model model) {
        if (!(model.containsAttribute("item"))) {
            model.addAttribute("item", this.dao.findEntityById(this.entityClass, id));
        }
        return urlPrefix + this.entityClass.getSimpleName().toLowerCase() + "/update";
    }

    public String form(Model model) throws InstantiationException, IllegalAccessException {
        if (!model.containsAttribute("item")) {
            model.addAttribute("item", entityClass.newInstance());
        }
        return urlPrefix + entityClass.getSimpleName().toLowerCase() + "/create";
    }

    public String update(E entity, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (!model.containsAttribute("item")) {
                model.addAttribute("item", entity);
            }
            model.addAttribute("errors", bindingResult.getAllErrors());
            return urlPrefix + entityClass.getSimpleName().toLowerCase() + "/update";
        }
        dao.merge(entity);
        return "redirect:" + urlPrefix + entityClass.getSimpleName().toLowerCase() + "/";
    }

    public String create(E entity, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (!model.containsAttribute("item")) {
                model.addAttribute("item", entity);
            }
            model.addAttribute("errors", bindingResult.getAllErrors());
            return urlPrefix + entityClass.getSimpleName().toLowerCase() + "/create";
        }
        dao.persist(entity);
        return "redirect:" + urlPrefix + entityClass.getSimpleName().toLowerCase() + "/";
    }

    public String delete(Long id) {
        dao.removeById(id);
        return "redirect:" + urlPrefix + entityClass.getSimpleName().toLowerCase() + "/";
    }
}
