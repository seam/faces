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

      assertTrue(Annotations.isAnnotationPresent(begin, Begin.class));
      assertFalse(Annotations.isAnnotationPresent(begin, End.class));
   }

   @Test
   public void testHasAnnotationOnMethodDirectly() throws Exception
   {
      Method end = AnnotationTestObject.class.getMethod("end", new Class[] {});

      assertTrue(Annotations.isAnnotationPresent(end, End.class));
   }

   @Test
   public void testHasAnnotationOnMethodIndirectlyFromClass() throws Exception
   {
      Method begin = AnnotationTestObject.class.getMethod("begin", new Class[] {});
      Method end = AnnotationTestObject.class.getMethod("end", new Class[] {});

      assertTrue(Annotations.isAnnotationPresent(begin, Begin.class));
      assertTrue(Annotations.isAnnotationPresent(end, Begin.class));
   }

   public void testGetAnnotationOnMethodDirectly() throws Exception
   {
      Method end = AnnotationTestObject.class.getMethod("end", new Class[] {});
      End anno = Annotations.getAnnotation(end, End.class);

      assertTrue(anno instanceof End);
   }

   public void testGetAnnotationOnMethodIndirectlyFromClass() throws Exception
   {
      Method end = AnnotationTestObject.class.getMethod("begin", new Class[] {});
      Begin anno = Annotations.getAnnotation(end, Begin.class);

      assertTrue(anno instanceof Begin);
   }

}
