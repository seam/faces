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

import org.jboss.logging.Logger;

/**
 * Extension that scans enums for view specific configuration
 * 
 * @author stuart
 * 
 */
public class ViewDataConfigurationExtension implements Extension
{

   private static final Logger log = Logger.getLogger(ViewDataConfigurationExtension.class);

   private final Map<String, Set<Annotation>> data = new HashMap<String, Set<Annotation>>();

   public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event)
   {
      AnnotatedType<T> tp = event.getAnnotatedType();
      if (tp.isAnnotationPresent(ViewConfig.class))
      {
         if (!tp.getJavaClass().isEnum())
         {
            log.warn("ViewConfig annotation should only be applied to enums, and [" + tp.getJavaClass() + "] is not an enum.");
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
