package org.jboss.seam.faces.lifecycle;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.jboss.seam.beans.BeanManagerHelper;
import org.jboss.seam.bridge.ManagerBridge;

/**
 * A JSF <strong>PhaseListener</strong> implementation that hooks Seam into the
 * JSF life cycle. This class observes all JSF phases and merely delegates to
 * an application-scope bean managed by JCDI.
 * 
 * @author Dan Allen
 */
public class SeamPhaseListener implements PhaseListener
{
   public void beforePhase(PhaseEvent event)
   {
      getDelegate().beforePhase(event);
   }
   
   public void afterPhase(PhaseEvent event)
   {
      getDelegate().afterPhase(event);
   }
   
   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

   protected ManagedSeamPhaseListener getDelegate()
   {
      return BeanManagerHelper.getInstanceByType(ManagerBridge.getProvider().getCurrentManager(),ManagedSeamPhaseListener.class);
   }

}
