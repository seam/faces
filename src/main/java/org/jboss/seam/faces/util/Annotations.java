package org.jboss.seam.faces.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.enterprise.inject.Stereotype;

/**
 * Utility class for common @{@link Annotation} operations.
 * <p>
 * TODO: This should probably go into weld-extensions so other portable
 * extensions can leverage it.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class Annotations
{
   /**
    * Discover if a Method <b>m</b> has been annotated with <b>type</b>. This
    * also discovers annotations defined through a @{@link Stereotype}.
    * 
    * @return True if annotation is present either on the method itself, or on
    *         the declaring class of the method. Returns false if the annotation
    *         is not present.
    */
   public static boolean hasAnnotation(final Method m, final Class<? extends Annotation> type)
   {
      boolean result = false;
      if (m.isAnnotationPresent(type))
      {
         result = true;
      }
      else
      {
         for (Annotation a : m.getAnnotations())
         {
            if (a.annotationType().isAnnotationPresent(type))
            {
               result = true;
            }
         }
      }

      if (result == false)
      {
         result = hasAnnotation(m.getDeclaringClass(), type);
      }
      return result;
   }

   /**
    * Discover if a Class <b>c</b> has been annotated with <b>type</b>. This
    * also discovers annotations defined through a @{@link Stereotype}.
    * 
    * @return True if annotation is present either on class, false if the
    *         annotation is not present.
    */
   public static boolean hasAnnotation(final Class<?> c, final Class<? extends Annotation> type)
   {
      boolean result = false;
      if (c.isAnnotationPresent(type))
      {
         result = true;
      }
      else
      {
         for (Annotation a : c.getAnnotations())
         {
            if (a.annotationType().isAnnotationPresent(type))
            {
               result = true;
            }
         }
      }
      return result;
   }
}
