package org.jboss.seam.faces.event;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;

@ApplicationScoped
public class SystemEventObserver
{
   public static boolean componentSystemEvent;
   public static boolean excecptionQueuedEvent;
   public static boolean postConstructApplicationEvent;
   public static boolean postConstructCustomScopeEvent;
   public static boolean preDestroyApplicationEvent;
   public static boolean preDestroyCustomScopeEvent;

   public void observeComponentSystemEvent(@Observes ComponentSystemEvent e) 
   {
      componentSystemEvent = true;
   }

   public void observeExceptionQueuedEvent(@Observes ExceptionQueuedEvent e) 
   {
      excecptionQueuedEvent = true;
   }

   public void observePostConstructApplicationEvent(@Observes PostConstructApplicationEvent e) 
   {
      postConstructApplicationEvent = true;
   }
   
   public void observePreDestroyApplicationEvent(@Observes PreDestroyApplicationEvent e) 
   {
      preDestroyApplicationEvent = true;
   }   
   
   public void observePostConstructCustomScopeEvent(@Observes PostConstructCustomScopeEvent e) 
   {
      postConstructCustomScopeEvent = true;
   }
   
   public void observePreDestroyCustomScopeEvent(@Observes PreDestroyCustomScopeEvent e) 
   {
      preDestroyCustomScopeEvent = true;
   }
}
