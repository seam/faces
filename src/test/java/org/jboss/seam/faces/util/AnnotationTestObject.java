package org.jboss.seam.faces.util;

import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.faces.context.conversation.End;

@Begin
public class AnnotationTestObject
{

   public void begin()
   {
   }

   @End
   public void end()
   {
   }

}
