package org.jboss.seam.faces.lifecycle;

import org.jboss.seam.faces.Faces;
import org.jboss.seam.international.StatusMessages;
import org.jboss.webbeans.log.Log;
import org.jboss.webbeans.log.Logger;

/**
 * <p>
 * A {@link FacesSystemEventProcessor} that is executed on a PreRenderViewEvent
 * or a redirect navigation event (via SeamViewHandler) to transpose Seam
 * StatusMessage objects into FacesMessage objects and transfer them to the
 * FacesContext.
 * </p>
 * 
 * <p>
 * FIXME the messages are going to get dropped if a view action causes a
 * navigation event followed by a redirect event
 * </p>
 * 
 * @author Dan Allen
 */
public class ConvertStatusMessagesProcessor implements FacesSystemEventProcessor
{
   @Logger Log log;
   
   @Faces StatusMessages statusMessages;

   public boolean execute()
   {
      statusMessages.onBeforeRender();
      return true;
   }
}
