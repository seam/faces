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

package org.jboss.seam.faces.environment;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ApplicationWrapper;
import javax.faces.convert.Converter;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.validator.Validator;
import javax.inject.Inject;

import org.jboss.seam.faces.event.PhaseEventBridge;

/**
 * Provides @{@link Inject} support for JSF artifacts such as {@link Converter},
 * {@link Validator}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
@ApplicationScoped
public class SeamApplicationWrapper extends ApplicationWrapper
{
   private Application parent;

   @Inject
   BeanManager manager;

   public void installWrapper(@Observes final PostConstructApplicationEvent event)
   {
      ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
      parent = factory.getApplication();
      factory.setApplication(this);
   }

   @Override
   public Converter createConverter(final Class<?> targetClass)
   {
      Converter result = null;

      Set<Bean<?>> beans = manager.getBeans(targetClass);
      if (!beans.isEmpty())
      {
         Bean<?> bean = beans.iterator().next();
         CreationalContext<?> context = manager.createCreationalContext(bean);
         result = (Converter) manager.getReference(bean, PhaseEventBridge.class, context);
      }

      if (result == null)
      {
         result = parent.createConverter(targetClass);
      }

      return result;
   }

   @Override
   public Converter createConverter(final String converterId)
   {
      /*
       * We need to ask for an instance because we have no way of getting the
       * type information by id
       */
      Converter result = parent.createConverter(converterId);
      if (result != null)
      {
         Class<? extends Converter> targetClass = result.getClass();
         Set<Bean<?>> beans = manager.getBeans(targetClass);
         if (!beans.isEmpty())
         {
            Bean<?> bean = beans.iterator().next();
            CreationalContext<?> context = manager.createCreationalContext(bean);
            result = (Converter) manager.getReference(bean, targetClass, context);
         }
      }

      return result;
   }

   @Override
   public Validator createValidator(final String validatorId)
   {
      /*
       * We need to ask for an instance because we have no way of getting the
       * type information by id
       */
      Validator result = parent.createValidator(validatorId);
      if (result != null)
      {
         Class<? extends Validator> targetClass = result.getClass();
         Set<Bean<?>> beans = manager.getBeans(targetClass);
         if (!beans.isEmpty())
         {
            Bean<?> bean = beans.iterator().next();
            CreationalContext<?> context = manager.createCreationalContext(bean);
            result = (Validator) manager.getReference(bean, targetClass, context);
         }
      }

      return result;
   }

   @Override
   public Application getWrapped()
   {
      return parent;
   }
}
