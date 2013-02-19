/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.springcrudutils.conversion;

import gr.dsigned.springcrudutils.types.SystemEntity;

/**
 * @author nk
 */
public interface ConverterFactory {

    public EntityDTOConverter getDTOConverter(Class<? extends SystemEntity> entityClass);
}
