package org.jboss.seam.faces.event;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.seam.faces.event.qualifier.ComponentSystemEvent;
import org.jboss.seam.faces.event.qualifier.ExceptionQueuedEvent;
import org.jboss.seam.faces.event.qualifier.PostConstructApplicationEvent;
import org.jboss.seam.faces.event.qualifier.PostConstructCustomScopeEvent;
import org.jboss.seam.faces.event.qualifier.PreDestroyApplicationEvent;
import org.jboss.seam.faces.event.qualifier.PreDestroyCustomScopeEvent;

@ApplicationScoped
public class SystemEventObserver
{
   public static boolean componentSystemEvent;
   public static boolean excecptionQueuedEvent;
   public static boolean postConstructApplicationEvent;
   public static boolean postConstructCustomScopeEvent;
   public static boolean preDestroyApplicationEvent;
   public static boolean preDestroyCustomScopeEvent;

   public void observeComponentSystemEvent(@Observes @ComponentSystemEvent javax.faces.event.ComponentSystemEvent e) 
   {
      componentSystemEvent = true;
   }

   public void observeExceptionQueuedEvent(@Observes @ExceptionQueuedEvent javax.faces.event.ExceptionQueuedEvent e) 
   {
      excecptionQueuedEvent = true;
   }

   public void observePostConstructApplicationEvent(@Observes @PostConstructApplicationEvent javax.faces.event.PostConstructApplicationEvent e) 
   {
      postConstructApplicationEvent = true;
   }
   
   public void observePreDestroyApplicationEvent(@Observes @PreDestroyApplicationEvent javax.faces.event.PreDestroyApplicationEvent e) 
   {
      preDestroyApplicationEvent = true;
   }   
   
   public void observePostConstructCustomScopeEvent(@Observes @PostConstructCustomScopeEvent javax.faces.event.PostConstructCustomScopeEvent e) 
   {
      postConstructCustomScopeEvent = true;
   }
   
   public void observePreDestroyCustomScopeEvent(@Observes @PreDestroyCustomScopeEvent javax.faces.event.PreDestroyCustomScopeEvent e) 
   {
      preDestroyCustomScopeEvent = true;
   }
}
