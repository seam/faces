package org.jboss.seam.faces.event;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.jboss.seam.faces.cdi.BeanManagerAware;

/**
 * Provide CDI injection to PhaseListener artifacts by delegating through this
 * class.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class DelegatingPhaseListener extends BeanManagerAware implements PhaseListener
{
   private static final long serialVersionUID = 8454616175394888259L;

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

   public void beforePhase(final PhaseEvent event)
   {
      for (PhaseListener listener : getPhaseListeners())
      {
         if (shouldProcessPhase(listener, event))
         {
            listener.beforePhase(event);
         }
      }
   }

   public void afterPhase(final PhaseEvent event)
   {
      for (PhaseListener listener : getPhaseListeners())
      {
         if (shouldProcessPhase(listener, event))
         {
            listener.afterPhase(event);
         }
      }
   }

   /**
    * Determine if the {@link PhaseListener} should process the given
    * {@link PhaseEvent}.
    */
   private boolean shouldProcessPhase(final PhaseListener listener, final PhaseEvent event)
   {
      return (PhaseId.ANY_PHASE.equals(listener.getPhaseId()) || event.getPhaseId().equals(listener.getPhaseId()));
   }

   @SuppressWarnings("unchecked")
   private List<PhaseListener> getPhaseListeners()
   {
      BeanManager manager = getBeanManager();
      List<PhaseListener> result = new ArrayList<PhaseListener>();

      Bean<PhaseEventBridge> bean = (Bean<PhaseEventBridge>) manager.getBeans(PhaseEventBridge.class).iterator().next();
      CreationalContext<PhaseEventBridge> context = manager.createCreationalContext(bean);
      PhaseEventBridge listener = (PhaseEventBridge) manager.getReference(bean, PhaseEventBridge.class, context);

      result.add(listener);

      return result;
   }

}
