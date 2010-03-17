package org.jboss.seam.faces.event;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.PhaseEvent;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.RenderResponse;

@ApplicationScoped
public class Observer
{
   public static boolean observeBeforeRenderResponse;
   public static boolean observeAfterRenderResponse;
   
   public void observeBeforeRenderResponse(@Observes @Before @RenderResponse PhaseEvent e) 
   {
      Observer.observeBeforeRenderResponse = true;
   }
   
   public void observeAfterRenderResponse(@Observes @After @RenderResponse PhaseEvent e) 
   {
      Observer.observeAfterRenderResponse = true;
   }
   
}

