package org.jboss.seam.faces.event;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.jboss.seam.faces.cdi.BeanManagerAware;

/**
 * Provide CDI injection to SystemEventListener artifacts by delegating through
 * this class.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class DelegatingSystemEventListener extends BeanManagerAware implements SystemEventListener
{

   public boolean isListenerForSource(final Object source)
   {
      return true;
   }

   public void processEvent(final SystemEvent event) throws AbortProcessingException
   {
      for (SystemEventListener l : getEventListeners())
      {
         if (l.isListenerForSource(event.getSource()))
         {
            l.processEvent(event);
         }
      }
   }

   @SuppressWarnings("unchecked")
   private List<SystemEventListener> getEventListeners()
   {
      BeanManager manager = getBeanManager();
      List<SystemEventListener> result = new ArrayList<SystemEventListener>();

      Bean<SystemEventBridge> bean = (Bean<SystemEventBridge>) manager.getBeans(SystemEventBridge.class).iterator().next();
      CreationalContext<SystemEventBridge> context = manager.createCreationalContext(bean);
      SystemEventBridge listener = (SystemEventBridge) manager.getReference(bean, SystemEventBridge.class, context);

      result.add(listener);

      return result;
   }

}
