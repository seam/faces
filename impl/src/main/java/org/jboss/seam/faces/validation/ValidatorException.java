package org.jboss.seam.faces.validation;

import javax.faces.application.FacesMessage;

/**
 * <p>
 * An extension of ValidatorException removing the need for creating 
 * new FacesMessage objects when throwing ValidatorExceptions from Validators.
 * </p>
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class ValidatorException extends javax.faces.validator.ValidatorException
{
    private static final long serialVersionUID = 1L;

    public ValidatorException(final String message)
    {
        super(new FacesMessage(message));
    }
}