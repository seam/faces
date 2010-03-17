package org.jboss.seam.faces.event;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.PhaseEvent;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;

@ApplicationScoped
public class PhaseListenerObserver
{
   public static boolean observeBeforeRenderResponse;
   public static boolean observeAfterRenderResponse;
   public static boolean observeBeforeApplyRequestValues;
   public static boolean observeAfterApplyRequestValues;
   public static boolean observeBeforeInvokeApplication;
   public static boolean observeAfterInvokeApplication;
   public static boolean observeBeforeProcessValidations;
   public static boolean observeAfterProcessValidations;
   public static boolean observeBeforeRestoreView;
   public static boolean observeAfterRestoreView;
   public static boolean observeBeforeUpdateModelValues;
   public static boolean observeAfterUpdateModelValues;
   
   public void observeBeforeRenderResponse(@Observes @Before @RenderResponse PhaseEvent e) 
   {
      PhaseListenerObserver.observeBeforeRenderResponse = true;
   }
   
   public void observeAfterRenderResponse(@Observes @After @RenderResponse PhaseEvent e) 
   {
      PhaseListenerObserver.observeAfterRenderResponse = true;
   }
   
   public void observeBeforeApplyRequestValues(@Observes @Before @ApplyRequestValues PhaseEvent e) 
   {
      PhaseListenerObserver.observeBeforeApplyRequestValues = true;
   }
   
   public void observeAfterApplyRequestValues(@Observes @After @ApplyRequestValues PhaseEvent e) 
   {
      PhaseListenerObserver.observeAfterApplyRequestValues = true;
   }
   
   public void observeBeforeInvokeApplication(@Observes @Before @InvokeApplication PhaseEvent e) 
   {
      PhaseListenerObserver.observeBeforeInvokeApplication = true;
   }
   
   public void observeAfterInvokeApplication(@Observes @After @InvokeApplication PhaseEvent e) 
   {
      PhaseListenerObserver.observeAfterInvokeApplication = true;
   }   
   
   public void observeBeforeProcessValidations(@Observes @Before @ProcessValidations PhaseEvent e) 
   {
      PhaseListenerObserver.observeBeforeProcessValidations = true;
   }
   
   public void observeAfterProcessValidations(@Observes @After @ProcessValidations PhaseEvent e) 
   {
      PhaseListenerObserver.observeAfterProcessValidations = true;
   }   
   
   public void observeBeforeRestoreView(@Observes @Before @RestoreView PhaseEvent e) 
   {
      PhaseListenerObserver.observeBeforeRestoreView = true;
   }
   
   public void observeAfterRestoreView(@Observes @After @RestoreView PhaseEvent e) 
   {
      PhaseListenerObserver.observeAfterRestoreView = true;
   }  

   public void observeBeforeUpdateModelValues(@Observes @Before @UpdateModelValues PhaseEvent e) 
   {
      PhaseListenerObserver.observeBeforeUpdateModelValues = true;
   }
   
   public void observeAfterUpdateModelValues(@Observes @After @UpdateModelValues PhaseEvent e) 
   {
      PhaseListenerObserver.observeAfterUpdateModelValues = true;
   }  
   
}
