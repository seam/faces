package org.jboss.seam.faces.view.config;

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
public class ViewConfigExtension implements Extension
{

   private static final Logger log = Logger.getLogger(ViewConfigExtension.class);

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
               if (f.isAnnotationPresent(ViewPattern.class))
               {
                  ViewPattern viewConfig = f.getAnnotation(ViewPattern.class);
                  Set<Annotation> viewPattern = new HashSet<Annotation>();
                  data.put(viewConfig.value(), viewPattern);
                  for (Annotation a : f.getAnnotations())
                  {
                     if (a.annotationType() != ViewPattern.class)
                     {
                        viewPattern.add(a);
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
