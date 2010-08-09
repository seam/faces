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
package org.jboss.seam.faces.viewdata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension that scans enums for view specific configuration
 * 
 * @author stuart
 * 
 */
public class ViewDataConfigurationExtension implements Extension
{

   private final Logger log = LoggerFactory.getLogger(ViewDataConfigurationExtension.class);

   private final Map<String, Set<Annotation>> data = new HashMap<String, Set<Annotation>>();

   public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event)
   {
      AnnotatedType<T> tp = event.getAnnotatedType();
      if (tp.isAnnotationPresent(ViewConfig.class))
      {
         if (!tp.getJavaClass().isEnum())
         {
            log.warn("ViewConfig annotation should only be applied to enums, and {} is not an enum.", tp.getJavaClass());
         }
         else
         {
            for (Field f : tp.getJavaClass().getDeclaredFields())
            {
               if (f.isAnnotationPresent(ViewData.class))
               {
                  ViewData viewConfig = f.getAnnotation(ViewData.class);
                  Set<Annotation> viewData = new HashSet<Annotation>();
                  data.put(viewConfig.value(), viewData);
                  for (Annotation a : f.getAnnotations())
                  {
                     if (a.annotationType() != ViewData.class)
                     {
                        viewData.add(a);
                     }
                  }
               }
            }
         }
      }
   }

   public Map<String, Set<Annotation>> getData()
   {
      return Collections.unmodifiableMap(data);
   }

}
