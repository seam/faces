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

      assertTrue(Annotations.has(begin, Begin.class));
      assertFalse(Annotations.has(begin, End.class));
   }

   @Test
   public void testHasAnnotationOnMethodDirectly() throws Exception
   {
      Method end = AnnotationTestObject.class.getMethod("end", new Class[] {});

      assertTrue(Annotations.has(end, End.class));
   }

   @Test
   public void testHasAnnotationOnMethodIndirectlyFromClass() throws Exception
   {
      Method begin = AnnotationTestObject.class.getMethod("begin", new Class[] {});
      Method end = AnnotationTestObject.class.getMethod("end", new Class[] {});

      assertTrue(Annotations.has(begin, Begin.class));
      assertTrue(Annotations.has(end, Begin.class));
   }

   public void testGetAnnotationOnMethodDirectly() throws Exception
   {
      Method end = AnnotationTestObject.class.getMethod("end", new Class[] {});
      End anno = Annotations.get(end, End.class);

      assertTrue(anno instanceof End);
   }

   public void testGetAnnotationOnMethodIndirectlyFromClass() throws Exception
   {
      Method end = AnnotationTestObject.class.getMethod("begin", new Class[] {});
      Begin anno = Annotations.get(end, Begin.class);

      assertTrue(anno instanceof Begin);
   }

}
