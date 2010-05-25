/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.faces.component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.component.FacesComponent;
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
import org.jboss.weld.extensions.beanManager.BeanManagerAccessor;

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

   private String validatorId = "";
   private String fields = "";
   private Map<String, UIInput> components = new HashMap<String, UIInput>();

   @Override
   public String getFamily()
   {
      return COMPONENT_FAMILY;
   }

   @Override
   public void validate(final FacesContext context)
   {
      context.getApplication().publishEvent(context, PreValidateEvent.class, UIValidateForm.class, this);
      BeanManager manager = BeanManagerAccessor.getManager();
      manager.fireEvent(this, BEFORE);

      Validator validator = context.getApplication().createValidator(validatorId);
      if (validator == null)
      {
         throw new IllegalArgumentException("Could not create Validator with id: [" + validatorId + "]");
      }

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
    * Attempt to locate the form in which this component resides. If the
    * component is not within a UIForm tag, throw an exception.
    */
   public UIForm locateForm()
   {
      UIComponent parent = this.getParent();
      while (!(parent instanceof UIForm))
      {
         if ((parent == null) || (parent instanceof UIViewRoot))
         {
            throw new IllegalStateException("The UIValidateForm (<s:validateForm />) component must be placed within a UIForm (<h:form>)");
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
      return fields;
   }

   public void setFields(final String fields)
   {
      this.fields = fields;
   }

   public String getValidatorId()
   {
      return validatorId;
   }

   public void setValidatorId(final String validatorId)
   {
      this.validatorId = validatorId;
   }

   /**
    * @param components
    */
   public void setComponents(final Map<String, UIInput> components)
   {
      this.components = components;
   }
}
