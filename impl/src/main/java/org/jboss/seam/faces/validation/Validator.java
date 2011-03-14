package org.jboss.seam.faces.validation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * <p>
 * A generic abstract class implementing Validator, for convenient removal of 
 * type casting.
 * </p>
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public abstract class Validator<T> implements javax.faces.validator.Validator
{
   FacesContext context;

   public abstract void validate(UIComponent component, T value) throws ValidatorException;

   @Override
   public void validate(final FacesContext context, final UIComponent component, final Object value) throws javax.faces.validator.ValidatorException
   {
      this.context = context;
      validate(component, (T) value);
   }

   public FacesContext getContext() {
      return context;
   }
}
