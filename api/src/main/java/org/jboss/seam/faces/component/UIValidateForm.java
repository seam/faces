package org.jboss.seam.faces.component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
@FacesComponent(UIValidateForm.COMPONENT_TYPE)
public class UIValidateForm extends UIInput
{
   private static final AnnotationLiteral<Before> BEFORE = new AnnotationLiteral<Before>()
   {
      private static final long serialVersionUID = 7631699535063526392L;
   };
   private static final AnnotationLiteral<After> AFTER = new AnnotationLiteral<After>()
   {
      private static final long serialVersionUID = -929128236303355107L;
   };

   public static final String COMPONENT_TYPE = "org.jboss.seam.faces.ValidateForm";
   public static final String COMPONENT_FAMILY = "org.jboss.seam.faces.ValidateForm";
   private static final String VALIDATOR_ID_KEY = COMPONENT_TYPE + "_ID_KEY";
   private static final Serializable COMPONENTS_MAP_KEY = COMPONENT_TYPE + "_COMPONENTS_MAP_KEY";
   private static final Serializable FIELDS_KEY = COMPONENT_TYPE + "_FIELDS_KEY";

   @Override
   public String getFamily()
   {
      return COMPONENT_FAMILY;
   }

   @Override
   public void validate(final FacesContext context)
   {
      context.getApplication().publishEvent(context, PreValidateEvent.class, UIValidateForm.class, this);
      BeanManager manager = new BeanManagerLocator().getBeanManager();
      manager.fireEvent(this, BEFORE);

      Validator validator = null;
      try
      {
         validator = context.getApplication().createValidator(getValidatorId());
         if (validator == null)
         {
            throw new IllegalArgumentException("Seam UIValidateForm - Could not create Validator with id: ["
                     + getValidatorId() + "]");
         }
      }
      catch (Exception e)
      {
         throw new IllegalStateException("Seam UIValidateForm - Could not create validator with id ["
                  + getValidatorId() + "] because: nested exception is:" + e.getMessage(), e);
      }

      Map<String, UIInput> components = getComponents();
      try
      {
         UIComponent parent = this.getParent();
         validator.validate(context, parent, components);
      }
      catch (ValidatorException e)
      {
         setValid(false);
         for (UIInput comp : components.values())
         {
            comp.setValid(false);
            // TODO Put this back when attributes can control it
            // context.addMessage(comp.getClientId(), e.getFacesMessage());
         }
         context.addMessage(null, e.getFacesMessage());
      }

      manager.fireEvent(this, AFTER);
      context.getApplication().publishEvent(context, PostValidateEvent.class, UIValidateForm.class, this);
   }

   /**
    * Attempt to locate the form in which this component resides. If the component is not within a UIForm tag, throw an
    * exception.
    */
   public UIForm locateForm()
   {
      UIComponent parent = this.getParent();
      while (!(parent instanceof UIForm))
      {
         if ((parent == null) || (parent instanceof UIViewRoot))
         {
            throw new IllegalStateException(
                     "The UIValidateForm (<s:validateForm />) component must be placed within a UIForm (<h:form>)");
         }
         parent = parent.getParent();
      }
      return (UIForm) parent;
   }

   /*
    * Prevent any rendered output.
    */

   @Override
   public void encodeAll(final FacesContext context) throws IOException
   {
      locateForm();
   }

   @Override
   public void encodeBegin(final FacesContext context) throws IOException
   {
   }

   @Override
   public void encodeEnd(final FacesContext context) throws IOException
   {
   }

   @Override
   public void encodeChildren(final FacesContext context) throws IOException
   {
   }

   /*
    * Getters & Setters
    */

   public String getFields()
   {
      StateHelper helper = this.getStateHelper(true);
      return (String) helper.get(FIELDS_KEY);
   }

   public void setFields(final String fields)
   {
      StateHelper helper = this.getStateHelper(true);
      helper.put(FIELDS_KEY, fields);
   }

   public String getValidatorId()
   {
      StateHelper helper = this.getStateHelper(true);
      return (String) helper.get(VALIDATOR_ID_KEY);
   }

   public void setValidatorId(final String validatorId)
   {
      StateHelper helper = this.getStateHelper(true);
      helper.put(VALIDATOR_ID_KEY, validatorId);
   }

   @SuppressWarnings("unchecked")
   private Map<String, UIInput> getComponents()
   {
      StateHelper helper = this.getStateHelper(true);
      return (Map<String, UIInput>) helper.get(COMPONENTS_MAP_KEY);
   }

   public void setComponents(final Map<String, UIInput> components)
   {
      StateHelper helper = this.getStateHelper(true);
      helper.put(COMPONENTS_MAP_KEY, components);
   }
}
