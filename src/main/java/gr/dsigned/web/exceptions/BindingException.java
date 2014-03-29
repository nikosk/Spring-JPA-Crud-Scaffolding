package gr.dsigned.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: nk Date: 2/13/14 Time: 12:18 AM
 */
public class BindingException extends RestException {

    private List<ObjectError> errors;


    public BindingException(String message, List<ObjectError> errors) {
        super(message, HttpStatus.BAD_REQUEST);
        this.errors = errors;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }
}
