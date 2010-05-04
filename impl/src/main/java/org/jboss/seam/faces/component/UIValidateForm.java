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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
@FacesComponent(UIValidateForm.COMPONENT_TYPE)
public class UIValidateForm extends UIInput
{
   public static final String COMPONENT_TYPE = "org.jboss.seam.faces.ValidateForm";
   public static final String COMPONENT_FAMILY = "org.jboss.seam.faces.ValidateForm";

   private String validatorId = "";
   private String fields = "";

   private final Logger log = LoggerFactory.getLogger(UIValidateForm.class);

   private final Map<String, UIInput> components = Collections.synchronizedMap(new HashMap<String, UIInput>());

   @Override
   public String getFamily()
   {
      return COMPONENT_FAMILY;
   }

   @Override
   public void validate(final FacesContext context)
   {
      UIComponent parent = this.getParent();
      while (!(parent instanceof UIForm))
      {
         parent = parent.getParent();
         if ((parent == null) || (parent instanceof UIViewRoot))
         {
            throw new IllegalStateException("The form validator must be placed within a UIForm");
         }
      }

      Validator validator = context.getApplication().createValidator(validatorId);
      if (validator == null)
      {
         throw new IllegalArgumentException("Could not create Validator with id: [" + validatorId + "]");
      }

      try
      {
         locateComponents(validator, parent, fields);
         injectFieldValues(validator, context);
         validator.validate(context, parent, components);
      }
      catch (ValidatorException e)
      {
         setComponentsInvalid();
         setValid(false);
         context.addMessage(null, e.getFacesMessage());
      }
   }

   public void locateComponents(final Validator validator, final UIComponent component, final String fields)
   {
      UIForm form = (UIForm) component;

      List<String> validatorFieldIds = getValidatorFieldNames(validator);

      if ((fields != null) && !"".equals(fields.trim()))
      {
         List<String> clientFieldIds = Arrays.asList(fields.split("\\s+"));
         for (String field : clientFieldIds)
         {
            List<String> mapping = Arrays.asList(field.split("\\s*=\\s*"));
            String clientValidatorFieldId = mapping.get(0);
            if (validatorFieldIds.contains(clientValidatorFieldId))
            {
               validatorFieldIds.remove(clientValidatorFieldId);
            }
            else
            {
               throw new IllegalArgumentException("Unknown field: [" + field + "] for Validator of type: " + validator.getClass().getName() + ", expected fields: " + getValidatorFieldNames(validator));
            }

            String clientFieldId = clientValidatorFieldId;

            if (mapping.size() > 1)
            {
               clientFieldId = mapping.get(1);
            }

            UIComponent comp = form.findComponent(clientFieldId);
            if ((comp != null) && (comp instanceof UIInput))
            {
               components.put(clientValidatorFieldId, (UIInput) comp);
            }
            else
            {
               log.warn("Could not locate component in form [" + form.getClientId() + ":" + clientValidatorFieldId + "]");
            }
         }
      }

      for (String field : validatorFieldIds)
      {
         UIComponent comp = form.findComponent(field);
         if ((comp != null) && (comp instanceof UIInput))
         {
            components.put(field, (UIInput) comp);
         }
         else
         {
            log.warn("Could not locate component in form [" + form.getClientId() + ":" + field + "] while processing Validator:" + validator.getClass().getName());
         }
      }
   }

   private void injectFieldValues(final Validator validator, final FacesContext context)
   {
      Field[] declaredFields = validator.getClass().getDeclaredFields();
      for (Field f : declaredFields)
      {
         String name = getFieldName(f);

         boolean restricted = false;
         if (!f.isAccessible())
         {
            restricted = true;
            f.setAccessible(true);
         }

         try
         {
            UIInput input = components.get(name);
            if (input != null)
            {
               Object value = input.getValue();
               f.set(validator, value);
            }
         }
         catch (Exception e)
         {
            throw new RuntimeException("Could not inject value into validator: " + validator.getClass().getName(), e);
         }

         if (restricted)
         {
            f.setAccessible(false);
         }
      }

      return;
   }

   private void setComponentsInvalid()
   {
      for (UIComponent comp : components.values())
      {
         if ((comp != null) && (comp instanceof UIInput))
         {
            UIInput input = (UIInput) comp;
            input.setValid(false);
         }
      }
   }

   private List<String> getValidatorFieldNames(final Validator validator)
   {
      List<String> result = new ArrayList<String>();
      Field[] declaredFields = validator.getClass().getDeclaredFields();
      for (Field f : declaredFields)
      {
         if (f.isAnnotationPresent(org.jboss.seam.faces.validation.InputField.class))
         {
            result.add(getFieldName(f));
         }
      }
      return result;
   }

   private String getFieldName(final Field f)
   {
      String name = "";
      if (f.isAnnotationPresent(org.jboss.seam.faces.validation.InputField.class))
      {
         name = f.getAnnotation(org.jboss.seam.faces.validation.InputField.class).value();
      }
      if ("".equals(name))
      {
         name = f.getName();
      }
      return name;
   }

   /*
    * Prevent any rendered output.
    */

   @Override
   public void encodeAll(final FacesContext context) throws IOException
   {
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
}
