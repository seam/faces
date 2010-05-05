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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.validation.InputField;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
@RequestScoped
public class FormValidationFieldProducer
{
   @Inject
   Logger log;

   @Inject
   FacesContext context;

   UIForm form = null;
   UIValidateForm validator = null;
   private Map<String, UIInput> components = null;

   public void interceptComponentTree(@Observes @Before final UIValidateForm event)
   {
      validator = event;
      form = locateForm(event);
      components = locateAliasedComponents(event);
   }

   public void cleanupComponentTree(@Observes @After final UIValidateForm event)
   {
      components = new HashMap<String, UIInput>();
   }

   @Produces
   @Dependent
   @InputField
   public Object getInputFieldValue(final InjectionPoint ip)
   {
      Object result = null;

      if (isInitialized())
      {
         String id = getFieldId(ip);
         UIInput component = findComponent(id, id);
         components.put(id, component);

         if (component.isLocalValueSet())
         {
            result = component.getValue();
         }
         else
         {
            Converter converter = component.getConverter();
            if (converter != null)
            {
               result = converter.getAsObject(context, component, (String) component.getSubmittedValue());
            }
            else
            {
               result = component.getSubmittedValue();
            }
         }

      }

      return result;
   }

   private boolean isInitialized()
   {
      return form != null;
   }

   private String getFieldId(final InjectionPoint ip)
   {
      String parameterName = ip.getAnnotated().getAnnotation(InputField.class).value();
      if ("".equals(parameterName))
      {
         parameterName = ip.getMember().getName();
      }
      return parameterName;
   }

   private UIForm locateForm(final UIComponent component)
   {
      UIComponent parent = component.getParent();
      while (!(parent instanceof UIForm))
      {
         parent = parent.getParent();
         if ((parent == null) || (parent instanceof UIViewRoot))
         {
            throw new IllegalStateException("The form validator must be placed within a UIForm");
         }
      }
      return (UIForm) parent;
   }

   public HashMap<String, UIInput> locateAliasedComponents(final UIValidateForm validator)
   {
      HashMap<String, UIInput> result = new HashMap<String, UIInput>();
      String fields = validator.getFields();
      if ((fields != null) && !"".equals(fields.trim()))
      {
         List<String> clientFieldIds = Arrays.asList(fields.split("\\s+"));
         for (String field : clientFieldIds)
         {
            List<String> mapping = Arrays.asList(field.split("\\s*=\\s*"));
            String aliasFieldName = mapping.get(0);

            String clientInputId = aliasFieldName;

            if (mapping.size() > 1)
            {
               clientInputId = mapping.get(1);
            }

            UIInput component = findComponent(aliasFieldName, clientInputId);
            components.put(aliasFieldName, component);
         }
      }
      return result;
   }

   private UIInput findComponent(final String alias, final String clientId)
   {
      UIComponent comp = null;
      if (!components.containsKey(clientId))
      {
         comp = form.findComponent(clientId);
         if (!(comp instanceof UIInput))
         {
            throw new IllegalArgumentException("Component [" + form.getClientId() + ":" + alias + "] must be a UIInput component.");
         }
         else if (comp == null)
         {
            throw new IllegalArgumentException("Could not locate component [" + form.getClientId() + ":" + alias + "]");
         }
      }
      else
      {
         comp = components.get(clientId);
      }
      return (UIInput) comp;
   }

}
