package org.jboss.seam.faces.status;

import java.io.Serializable;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.event.PhaseEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.context.RenderContext;
import org.jboss.seam.faces.event.PreNavigateEvent;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.international.status.Level;
import org.jboss.seam.international.status.Message;
import org.jboss.seam.international.status.Messages;

/**
 * Convert Seam Messages into FacesMessages before RenderResponse phase.<br>
 * TODO perform EL evaluation.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class MessagesAdapter implements Serializable
{
   private static final long serialVersionUID = -2908193057765795662L;
   private static final Logger log = Logger.getLogger(MessagesAdapter.class);

   private static final String FLASH_MESSAGES_KEY = MessagesAdapter.class.getName() + ".FLASH_KEY";

   @Inject
   Messages messages;

   @Inject
   RenderContext context;

   void flushBeforeNavigate(@Observes final PreNavigateEvent event)
   {
      if (!messages.getAll().isEmpty())
      {
         log.debug("Saving status Messages to Flash Scope");
         context.put(FLASH_MESSAGES_KEY, messages.getAll());
         messages.clear();
      }
   }

   @SuppressWarnings("unchecked")
   void convert(@Observes @Before @RenderResponse final PhaseEvent event)
   {
      Set<Message> savedMessages = (Set<Message>) context.get(FLASH_MESSAGES_KEY);
      if (savedMessages != null)
      {
         for (Message m : savedMessages)
         {
            event.getFacesContext().addMessage(m.getTargets(), new FacesMessage(getSeverity(m.getLevel()), m.getText(), null));
         }
      }

      for (Message m : messages.getAll())
      {
         event.getFacesContext().addMessage(m.getTargets(), new FacesMessage(getSeverity(m.getLevel()), m.getText(), null));
      }
      messages.clear();
   }

   private Severity getSeverity(final Level level)
   {
      Severity result = FacesMessage.SEVERITY_INFO;
      switch (level)
      {
      case INFO:
         break;
      case WARN:
         result = FacesMessage.SEVERITY_WARN;
         break;
      case ERROR:
         result = FacesMessage.SEVERITY_ERROR;
         break;
      case FATAL:
         result = FacesMessage.SEVERITY_FATAL;
         break;
      default:
         break;
      }
      return result;
   }

}
