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
public class Observer
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
      Observer.observeBeforeRenderResponse = true;
   }
   
   public void observeAfterRenderResponse(@Observes @After @RenderResponse PhaseEvent e) 
   {
      Observer.observeAfterRenderResponse = true;
   }
   
   public void observeBeforeApplyRequestValues(@Observes @Before @ApplyRequestValues PhaseEvent e) 
   {
      Observer.observeBeforeApplyRequestValues = true;
   }
   
   public void observeAfterApplyRequestValues(@Observes @After @ApplyRequestValues PhaseEvent e) 
   {
      Observer.observeAfterApplyRequestValues = true;
   }
   
   public void observeBeforeInvokeApplication(@Observes @Before @InvokeApplication PhaseEvent e) 
   {
      Observer.observeBeforeInvokeApplication = true;
   }
   
   public void observeAfterInvokeApplication(@Observes @After @InvokeApplication PhaseEvent e) 
   {
      Observer.observeAfterInvokeApplication = true;
   }   
   
   public void observeBeforeProcessValidations(@Observes @Before @ProcessValidations PhaseEvent e) 
   {
      Observer.observeBeforeProcessValidations = true;
   }
   
   public void observeAfterProcessValidations(@Observes @After @ProcessValidations PhaseEvent e) 
   {
      Observer.observeAfterProcessValidations = true;
   }   
   
   public void observeBeforeRestoreView(@Observes @Before @RestoreView PhaseEvent e) 
   {
      Observer.observeBeforeRestoreView = true;
   }
   
   public void observeAfterRestoreView(@Observes @After @RestoreView PhaseEvent e) 
   {
      Observer.observeAfterRestoreView = true;
   }  

   public void observeBeforeUpdateModelValues(@Observes @Before @UpdateModelValues PhaseEvent e) 
   {
      Observer.observeBeforeUpdateModelValues = true;
   }
   
   public void observeAfterUpdateModelValues(@Observes @After @UpdateModelValues PhaseEvent e) 
   {
      Observer.observeAfterUpdateModelValues = true;
   }  
   
}
