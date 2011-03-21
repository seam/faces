package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 * 
 */
public class ViewConfigExtension implements Extension
{

   private static final Logger log = Logger.getLogger(ViewConfigExtension.class);

   private final Map<String, Set<Annotation>> data = new HashMap<String, Set<Annotation>>();

   public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event)
   {
      AnnotatedType<T> tp = event.getAnnotatedType();
      if (log.isTraceEnabled()) {
          log.tracef("Annotated Type: %s", tp.getJavaClass().getName());
          for (Annotation annotation : tp.getAnnotations()) {
              log.tracef("|-- Annotation: %s", annotation.annotationType().getName());
          }
      }
      if (tp.isAnnotationPresent(ViewConfig.class))
      {
         if (!tp.getJavaClass().isInterface())
         {
            log.warn("ViewConfig annotation should only be applied to interfaces, and [" + tp.getJavaClass() + "] is not an interface.");
         }
         else
         {
            for (Method method : tp.getJavaClass().getDeclaredMethods())
            {
               if (method.isAnnotationPresent(ViewPattern.class))
               {
                  ViewPattern viewConfig = method.getAnnotation(ViewPattern.class);
                  Set<Annotation> viewPattern = new HashSet<Annotation>();
                  data.put(viewConfig.value(), viewPattern);
                  for (Annotation a : method.getAnnotations())
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