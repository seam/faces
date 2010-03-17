package org.jboss.seam.faces.event;

import java.lang.annotation.Annotation;

import javax.enterprise.util.AnnotationLiteral;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;

public class PhaseEventListener extends GenericEventListener implements PhaseListener
{
   private static final long serialVersionUID = 1L;

   private enum When
   {
      BEFORE, AFTER
   };

   @SuppressWarnings("serial")
   private void handlePhase(When when, PhaseEvent e)
   {
      Annotation whenAnnotation = null;
      Annotation phaseAnnotation = null;
      switch (when)
      {
      case BEFORE:
         whenAnnotation = new AnnotationLiteral<Before>()
         {
         };
         break;
      case AFTER:
         whenAnnotation = new AnnotationLiteral<After>()
         {
         };
         break;
      }
      if (e.getPhaseId() == PhaseId.APPLY_REQUEST_VALUES)
      {
         phaseAnnotation = new AnnotationLiteral<ApplyRequestValues>()
         {
         };
      }
      else if (e.getPhaseId() == PhaseId.INVOKE_APPLICATION)
      {
         phaseAnnotation = new AnnotationLiteral<InvokeApplication>()
         {
         };
      }
      else if (e.getPhaseId() == PhaseId.PROCESS_VALIDATIONS)
      {
         phaseAnnotation = new AnnotationLiteral<ProcessValidations>()
         {
         };
      }
      else if (e.getPhaseId() == PhaseId.RENDER_RESPONSE)
      {
         phaseAnnotation = new AnnotationLiteral<RenderResponse>()
         {
         };
      }
      else if (e.getPhaseId() == PhaseId.RESTORE_VIEW)
      {
         phaseAnnotation = new AnnotationLiteral<RestoreView>()
         {
         };
      }
      else if (e.getPhaseId() == PhaseId.UPDATE_MODEL_VALUES)
      {
         phaseAnnotation = new AnnotationLiteral<UpdateModelValues>()
         {
         };
      }
      getBeanManager().fireEvent(e, new Annotation[] { whenAnnotation, phaseAnnotation });
   }

   public void afterPhase(PhaseEvent e)
   {
      handlePhase(When.AFTER, e);
   }

   public void beforePhase(PhaseEvent e)
   {
      handlePhase(When.BEFORE, e);
   }

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

}
