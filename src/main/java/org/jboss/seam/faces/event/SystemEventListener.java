package org.jboss.seam.faces.event;

import java.lang.annotation.Annotation;

import javax.enterprise.util.AnnotationLiteral;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.SystemEvent;

public class SystemEventListener extends GenericEventListener implements javax.faces.event.SystemEventListener
{
   public boolean isListenerForSource(Object source)
   {
      return true;
   }

   @SuppressWarnings("serial")
   public void processEvent(SystemEvent e) throws AbortProcessingException
   {
      Object payload = e.getClass().cast(e);
      Annotation qualifier = null;
      if (e instanceof ComponentSystemEvent)
      {
         qualifier = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.ComponentSystemEvent>()
         {
         };
      }
      else if (e instanceof ExceptionQueuedEvent)
      {
         qualifier = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.ExceptionQueuedEvent>()
         {
         };
      }
      else if (e instanceof PostConstructApplicationEvent)
      {
         qualifier = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PostConstructApplicationEvent>()
         {
         };
      }
      else if (e instanceof PostConstructCustomScopeEvent)
      {
         qualifier = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PostConstructCustomScopeEvent>()
         {
         };
      }
      else if (e instanceof PreDestroyApplicationEvent)
      {
         qualifier = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PreDestroyApplicationEvent>()
         {
         };
      }
      else if (e instanceof PreDestroyCustomScopeEvent)
      {
         qualifier = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PreDestroyCustomScopeEvent>()
         {
         };
      }
      getBeanManager().fireEvent(payload, qualifier);
   }

}
