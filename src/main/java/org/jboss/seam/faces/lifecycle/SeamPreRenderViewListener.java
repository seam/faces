package org.jboss.seam.faces.lifecycle;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 * <p>
 * A JSF {@link SystemEventListener} that observes the PreRenderViewEvent, which
 * is fired in the Render Response phase of the JSF life cycle immediately prior
 * to rendering the view. This listener maintains a list of
 * {@link FacesSystemEventProcessor} types that are resolved by the JSR-299
 * BeanManager and executed in turn. The execute() method of each processor
 * indicates whether to continue on to the next processor or not.
 * </p>
 * 
 * @author Dan Allen
 * @see FacesSystemEventProcessor
 */
//@ListenerFor(systemEventClass = PreRenderViewEvent.class, sourceClass = UIViewRoot.class)
public class SeamPreRenderViewListener implements SystemEventListener
{
   private List<Class<? extends FacesSystemEventProcessor>> processorTypes = new ArrayList<Class<? extends FacesSystemEventProcessor>>();
   
   public SeamPreRenderViewListener()
   {
      processorTypes.add(EnforceViewRestrictionsProcessor.class);
      processorTypes.add(ExecuteViewActionsListener.class);
      processorTypes.add(ImportNamespacesProcessor.class);
      processorTypes.add(ConvertStatusMessagesProcessor.class);
   }
   
   public boolean isListenerForSource(Object source)
   {
      return source instanceof UIViewRoot;
   }

   public void processEvent(SystemEvent event) throws AbortProcessingException
   {
//      BeanManager manager = ManagerBridge.getProvider().getCurrentManager();
//      for (Class<? extends FacesSystemEventProcessor> processorType : processorTypes)
//      {
//         boolean result = BeanManagerHelper.getInstanceByType(manager, processorType).execute();
//         if (!result)
//         {
//            break;
//         }
//      }
   }

}
