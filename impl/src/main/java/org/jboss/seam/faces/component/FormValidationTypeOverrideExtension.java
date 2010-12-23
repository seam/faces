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

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.seam.faces.validation.InputField;
import org.jboss.seam.solder.reflection.annotated.AnnotatedTypeBuilder;

/**
 * Ensure that any field annotated with {@link InputField} is produced by the
 * same producer method with output type {@link Object}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
@ApplicationScoped
public class FormValidationTypeOverrideExtension implements Extension
{
   private final Map<Class<?>, AnnotatedType<?>> typeOverrides = new HashMap<Class<?>, AnnotatedType<?>>();

   public <T> void processAnnotatedType(@Observes final ProcessAnnotatedType<T> event)
   {
      AnnotatedTypeBuilder<T> builder = new AnnotatedTypeBuilder<T>();
      builder.readFromType(event.getAnnotatedType());

      boolean modifiedType = false;

      for (AnnotatedField<?> f : event.getAnnotatedType().getFields())
      {
         if (f.isAnnotationPresent(InputField.class))
         {
            builder.overrideFieldType(f.getJavaMember(), Object.class);
            modifiedType = true;
         }
      }

      if (modifiedType)
      {
         AnnotatedType<T> replacement = builder.create();
         typeOverrides.put(replacement.getJavaClass(), replacement);
         event.setAnnotatedType(replacement);
      }
   }

   public boolean hasOverriddenType(final Class<?> clazz)
   {
      return typeOverrides.containsKey(clazz);
   }

   public AnnotatedType<?> getOverriddenType(final Class<?> clazz)
   {
      return typeOverrides.get(clazz);
   }
}
