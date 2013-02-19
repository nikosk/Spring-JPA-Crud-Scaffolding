/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.springcrudutils.conversion;

import gr.dsigned.springcrudutils.types.SystemDTO;
import gr.dsigned.springcrudutils.types.SystemEntity;

/**
 * @author nk
 */
public interface EntityDTOConverter<E extends SystemEntity, D extends SystemDTO> {

    public D convertToDTO(E entity);

    public E convertToEntity(D dto);
}
