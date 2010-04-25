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
package org.jboss.seam.faces.event;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;
import org.slf4j.Logger;

/**
 * A PhaseListener used to bridge JSF phase events to the CDI event model.
 * <p>
 * 
 * For each JSF {@link PhaseEvent}, a corresponding Seam CDI event will be
 * fired. Event listeners can be registered by observing the appropriate Seam
 * CDI event (see @{@link Observes}):
 * <p>
 * <b>For example:</b>
 * <p>
 * <code>
 * public void listener(@Observes @Before @RenderResponse PhaseEvent event)
 * {
 *    //do something
 * }
 * </code>
 * 
 * @author Nicklas Karlsson
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class PhaseEventBridge implements PhaseListener
{
   private static final long serialVersionUID = -6181019551463318453L;

   @Inject
   private Logger log;
   
   @Inject
   private BeanManager beanManager;

   /**
    * @param whenQualifier When this event occurred (e.g.:
    *           {@link PhaseListener#beforePhase(PhaseEvent)} or
    *           {@link PhaseListener#afterPhase(PhaseEvent)})
    * @param event The JSF PhaseEvent to be propagated
    */
   private void handlePhase(final AnnotationLiteral<?> whenQualifier, final PhaseEvent event)
   {
      Annotation phaseQualifier = null;

      if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId()))
      {
         phaseQualifier = RESTORE_VIEW;
      }
      else if (PhaseId.PROCESS_VALIDATIONS.equals(event.getPhaseId()))
      {
         phaseQualifier = PROCESS_VALIDATIONS;
      }
      else if (PhaseId.APPLY_REQUEST_VALUES.equals(event.getPhaseId()))
      {
         phaseQualifier = APPLY_REQUEST_VALUES;
      }
      else if (PhaseId.INVOKE_APPLICATION.equals(event.getPhaseId()))
      {
         phaseQualifier = INVOKE_APPLICATION;
      }
      else if (PhaseId.UPDATE_MODEL_VALUES.equals(event.getPhaseId()))
      {
         phaseQualifier = UPDATE_MODEL_VALUES;
      }
      else if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId()))
      {
         phaseQualifier = RENDER_RESPONSE;
      }
      else
      {
         log.error("Unknown JSF PhaseId detected during CDI event broadcasting");
      }

      /*
       * This propagates the event to CDI
       */
      Annotation[] qualifiers = new Annotation[] { whenQualifier, phaseQualifier };
      log.debug("Fired event #0 with qualifiers #1", event, qualifiers);
      beanManager.fireEvent(event, qualifiers);
   }

   public void afterPhase(final PhaseEvent e)
   {
      handlePhase(AFTER, e);
   }

   public void beforePhase(final PhaseEvent e)
   {
      handlePhase(BEFORE, e);
   }

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

   /*
    * Annotation Literal Constants
    */
   private static final AnnotationLiteral<Before> BEFORE = new AnnotationLiteral<Before>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<After> AFTER = new AnnotationLiteral<After>()
   {
      private static final long serialVersionUID = 5121252401235504952L;
   };

   private static final AnnotationLiteral<RestoreView> RESTORE_VIEW = new AnnotationLiteral<RestoreView>()
   {
      private static final long serialVersionUID = 8812020629644833820L;
   };

   private static final AnnotationLiteral<ProcessValidations> PROCESS_VALIDATIONS = new AnnotationLiteral<ProcessValidations>()
   {
      private static final long serialVersionUID = 8637149472340997800L;
   };

   private static final AnnotationLiteral<ApplyRequestValues> APPLY_REQUEST_VALUES = new AnnotationLiteral<ApplyRequestValues>()
   {
      private static final long serialVersionUID = 8558744089046159077L;
   };

   private static final AnnotationLiteral<InvokeApplication> INVOKE_APPLICATION = new AnnotationLiteral<InvokeApplication>()
   {
      private static final long serialVersionUID = 3161037426109802030L;
   };

   private static final AnnotationLiteral<UpdateModelValues> UPDATE_MODEL_VALUES = new AnnotationLiteral<UpdateModelValues>()
   {
      private static final long serialVersionUID = -2714189905299700793L;
   };

   private static final AnnotationLiteral<RenderResponse> RENDER_RESPONSE = new AnnotationLiteral<RenderResponse>()
   {
      private static final long serialVersionUID = -8708300190197778734L;
   };

}
