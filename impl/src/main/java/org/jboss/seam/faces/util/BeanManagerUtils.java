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

package org.jboss.seam.faces.util;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import org.jboss.seam.faces.component.FormValidationTypeOverrideExtension;

/**
 * A utility providing common functions to simply use of {@link BeanManager}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class BeanManagerUtils
{
   @Inject
   private BeanManager manager;

   @Inject
   FormValidationTypeOverrideExtension classExtension;

   /**
    * Perform @{@link Inject} on an object as if it were a bean managed by CDI.
    * 
    * @param instance
    */
   @SuppressWarnings("unchecked")
   public void injectNonContextualInstance(final Object instance)
   {
      if (instance != null)
      {
         CreationalContext<Object> creationalContext = manager.createCreationalContext(null);
         InjectionTarget<Object> injectionTarget = (InjectionTarget<Object>) manager.createInjectionTarget(getAnnotatedType(instance));
         injectionTarget.inject(instance, creationalContext);
      }
   }

   private AnnotatedType<? extends Object> getAnnotatedType(final Object instance)
   {
      AnnotatedType<?> result = null;
      if (classExtension.hasOverriddenType(instance.getClass()))
      {
         result = classExtension.getOverriddenType(instance.getClass());
      }
      else
      {
         result = manager.createAnnotatedType(instance.getClass());
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   public <T> boolean isDependentScoped(final Class<T> type)
   {
      Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
      if (bean != null)
      {
         return Dependent.class.equals(bean.getScope());
      }
      return false;
   }

   /**
    * Get a single CDI managed instance of a specific class. Return only the
    * first result if multiple beans are available.
    * 
    * @param type The class for which to return an instance.
    * @return The managed instance, or null if none could be provided.
    */
   @SuppressWarnings("unchecked")
   public <T> T getContextualInstance(final Class<T> type)
   {
      Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
      if (bean != null)
      {
         CreationalContext<T> context = manager.createCreationalContext(bean);
         T result = (T) manager.getReference(bean, type, context);
         return result;
      }
      return null;
   }

   /**
    * Get all CDI managed instances of a specific class. Return results in a
    * {@link List} in no specific order.
    * 
    * @param type The class for which to return instances.
    */
   @SuppressWarnings("unchecked")
   public <T> List<T> getContextualInstances(final Class<T> type)
   {
      List<T> result = new ArrayList<T>();
      for (Bean<?> bean : manager.getBeans(type))
      {
         CreationalContext<T> context = (CreationalContext<T>) manager.createCreationalContext(bean);
         result.add((T) manager.getReference(bean, type, context));
      }
      return result;
   }
}
