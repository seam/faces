package org.jboss.seam.faces.event;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.jboss.seam.faces.SeamFacesException;
import org.jboss.seam.faces.cdi.BeanManagerAware;

/**
 * A SystemEventListener used to bridge JSF system events to the CDI event
 * model.
 * <p>
 * 
 * For each JSF system event (e.g: {@link PostConstructApplicationEvent}, a
 * corresponding Seam CDI event will be fired.
 * <p>
 * 
 * Event listeners can be registered by observing the appropriate Seam CDI event
 * (see @{@link Observes}):
 * <p>
 * <b>For example:</b>
 * <p>
 * <code>
 * public void listener(@Observes org.jboss.seam.faces.event.qualifier.ExceptionQueuedEvent event)
 * {
 *    //do something
 * }
 * </code>
 * 
 * @author Nicklas Karlsson
 */
@ApplicationScoped
public class SystemEventBridge extends BeanManagerAware implements SystemEventListener
{

   public boolean isListenerForSource(final Object source)
   {
      return true;
   }

   public void processEvent(final SystemEvent e) throws AbortProcessingException
   {
      Object payload = e.getClass().cast(e);
      Annotation qualifier = null;

      if (e instanceof ComponentSystemEvent)
      {
         qualifier = COMPONENT_SYSTEM_EVENT;
      }
      else if (e instanceof ExceptionQueuedEvent)
      {
         qualifier = EXCEPTION_QUEUED_EVENT;
      }
      else if (e instanceof PostConstructApplicationEvent)
      {
         qualifier = POST_CONSTRUCT_APPLICATION_EVENT;
      }
      else if (e instanceof PostConstructCustomScopeEvent)
      {
         qualifier = POST_CONSTRUCT_CUSTOM_SCOPE_EVENT;
      }
      else if (e instanceof PreDestroyApplicationEvent)
      {
         qualifier = PRE_DESTROY_APPLICATION_EVENT;
      }
      else if (e instanceof PreDestroyCustomScopeEvent)
      {
         qualifier = PRE_DESTROY_CUSTOM_SCOPE_EVENT;
      }
      else
      {
         throw new SeamFacesException("Unknown JSF System Event detected during CDI event broadcasting");
      }

      /*
       * This propagates the event to CDI
       */
      getBeanManager().fireEvent(payload, qualifier);
   }

   /*
    * System Event Annotations
    */
   private static final AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PreDestroyCustomScopeEvent> PRE_DESTROY_CUSTOM_SCOPE_EVENT = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PreDestroyCustomScopeEvent>()
   {
      private static final long serialVersionUID = -7243409955575081242L;
   };
   private static final AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PreDestroyApplicationEvent> PRE_DESTROY_APPLICATION_EVENT = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PreDestroyApplicationEvent>()
   {
      private static final long serialVersionUID = -7448942843812054204L;
   };
   private static final AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PostConstructCustomScopeEvent> POST_CONSTRUCT_CUSTOM_SCOPE_EVENT = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PostConstructCustomScopeEvent>()
   {
      private static final long serialVersionUID = 6194794712139598271L;
   };
   private static final AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PostConstructApplicationEvent> POST_CONSTRUCT_APPLICATION_EVENT = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.PostConstructApplicationEvent>()
   {
      private static final long serialVersionUID = -7119316486394672512L;
   };
   private static final AnnotationLiteral<org.jboss.seam.faces.event.qualifier.ExceptionQueuedEvent> EXCEPTION_QUEUED_EVENT = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.ExceptionQueuedEvent>()
   {
      private static final long serialVersionUID = -2694169811275854595L;
   };
   private static final AnnotationLiteral<org.jboss.seam.faces.event.qualifier.ComponentSystemEvent> COMPONENT_SYSTEM_EVENT = new AnnotationLiteral<org.jboss.seam.faces.event.qualifier.ComponentSystemEvent>()
   {
      private static final long serialVersionUID = -8018221003951485295L;
   };

}
