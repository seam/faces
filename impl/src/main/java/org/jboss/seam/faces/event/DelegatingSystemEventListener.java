package org.jboss.seam.faces.event;

import java.util.List;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 * Provide CDI injection to SystemEventListener artifacts by delegating through
 * this class.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class DelegatingSystemEventListener extends AbstractListener<SystemEventListener> implements SystemEventListener
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
      return getListeners(SystemEventBridge.class);
   }

}
