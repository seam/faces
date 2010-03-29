package org.jboss.seam.faces.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.faces.context.conversation.End;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class AnnotationsTest
{
   @Test
   public void testHasAnnotationOnClassDirectly() throws Exception
   {
      Method begin = AnnotationTestObject.class.getMethod("begin", new Class[] {});

      assertTrue(Annotations.hasAnnotation(begin, Begin.class));
      assertFalse(Annotations.hasAnnotation(begin, End.class));
   }

   @Test
   public void testHasAnnotationOnMethodDirectly() throws Exception
   {
      Method end = AnnotationTestObject.class.getMethod("end", new Class[] {});

      assertTrue(Annotations.hasAnnotation(end, End.class));
   }

   @Test
   public void testHasAnnotationOnMethodIndirectlyFromClass() throws Exception
   {
      Method begin = AnnotationTestObject.class.getMethod("begin", new Class[] {});
      Method end = AnnotationTestObject.class.getMethod("end", new Class[] {});

      assertTrue(Annotations.hasAnnotation(begin, Begin.class));
      assertTrue(Annotations.hasAnnotation(end, Begin.class));
   }

}
