package org.jboss.seam.faces.international;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.seam.faces.Faces;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessagesDelegate;
import org.jboss.webbeans.log.Log;
import org.jboss.webbeans.log.Logger;

/**
 * <p>
 * A bean which wraps the generic StatusMessages component to provide support
 * for JSF. The StatusMessage objects are translated to JSF FacesMessage objects
 * and registered with the FacesContext by this class prior to view rendering or
 * a servlet redirect.
 * </p>
 * 
 * <p>
 * To access the JSF FacesMessage objects once they have been registered with
 * the FacesContext, you should use the methods getMessageList() and
 * getMessagesList(String), which were added to FacesContext in JSF 2.0.
 * </p>
 * 
 * <p>
 * For instance, to retrieve the list of global messages, you can use the
 * following value expression in your page:
 * </p>
 * 
 * <pre>
 * #{facesContext.getMessageList(null)}
 * </pre>
 * 
 * <p>
 * To retrieve the list of messages for a "client id", you can use this
 * expression:
 * </p>
 * 
 * <pre>
 * #{facesContext.getMessageList('username')}
 * </pre>
 * 
 * <p>
 * These examples rely, of course, on the JBoss EL.
 * </p>
 * 
 * @author Gavin King
 * @author Pete Muir
 * @author Dan Allen
 */
public
@Faces
class FacesMessages extends StatusMessagesDelegate
{
   @Logger Log log;
   
   private StatusMessages statusMessages;

   public @Inject FacesMessages(StatusMessages statusMessages)
   {
      this.statusMessages = statusMessages;
   }

   @Override
   public StatusMessages getStatusMessages()
   {
      return this.statusMessages;
   }

   /**
    * A convenience method to convert a FacesMessage into a StatusMessage and
    * register it as a global message. It may be that the application receives a
    * FacesMessage object from some part of the system and needs to join it with
    * the current set of StatusMessage objects.
    * 
    * @param message A populated JSF FacesMessage object
    */
   public void add(FacesMessage message)
   {
      add(toSeverity(message.getSeverity()), null, null, message.getSummary(), message.getDetail());
   }

   /**
    * A convenience method to convert a FacesMessage into a StatusMessage and
    * register it with a control. It may be that the application receives a
    * FacesMessage object from some part of the system and needs to join it with
    * the current set of StatusMessage objects.
    * 
    * @param id The client id of the target component
    * @param message A populated JSF FacesMessage object
    */
   public void addToControl(String id, FacesMessage message)
   {
      addToControl(id, toSeverity(message.getSeverity()), null, null, message.getSummary(), message.getDetail());
   }
   
   /**
    * Called by a JSF SystemEventListener listening for the PreRenderViewEvent
    * to transpose Seam status messages to real JSF messages and register them
    * with the FacesContext.
    */
   @Override
   public void onBeforeRender()
   {
      super.onBeforeRender();
      FacesContext facesContext = FacesContext.getCurrentInstance();
      for (StatusMessage statusMessage : getGlobalMessages())
      {
         facesContext.addMessage(null, toFacesMessage(statusMessage));
      }
      for (Map.Entry<String, List<StatusMessage>> messagesForKey : getKeyedMessages().entrySet())
      {
         String clientId = getClientId(messagesForKey.getKey(), facesContext);
         if (clientId == null)
         {
            log.warn("Could not locate control '" + messagesForKey.getKey() + "' when registering JSF message. A global message will be created as a fallback.");
         }
         for (StatusMessage statusMessage : messagesForKey.getValue())
         {
            facesContext.addMessage(clientId, toFacesMessage(statusMessage));
         }
      }
      clear();
   }

   /**
    * Convert a StatusMessage to a FacesMessage
    */
   private FacesMessage toFacesMessage(StatusMessage statusMessage)
   {
      if (statusMessage.getSummary() != null && statusMessage.getSummary().length() > 0)
      {
         return new FacesMessage(toFacesSeverity(statusMessage.getSeverity()), statusMessage.getSummary(), statusMessage.getDetail());
      }
      else
      {
         return null;
      }
   }

   /**
    * Convert a FacesMessage.Severity to a StatusMessage.Severity
    */
   private static StatusMessage.Severity toSeverity(FacesMessage.Severity severity)
   {
      if (FacesMessage.SEVERITY_ERROR.equals(severity))
      {
         return StatusMessage.Severity.ERROR;
      }
      else if (FacesMessage.SEVERITY_FATAL.equals(severity))
      {
         return StatusMessage.Severity.FATAL;
      }
      else if (FacesMessage.SEVERITY_WARN.equals(severity))
      {
         return StatusMessage.Severity.WARN;
      }
      else
      {
         return StatusMessage.Severity.INFO;
      }
   }
   
   /**
    * Convert a StatusMessage.Severity to a FacesMessage.Severity
    */
   private FacesMessage.Severity toFacesSeverity(StatusMessage.Severity severity)
   {
      switch (severity)
      {
         case ERROR:
            return FacesMessage.SEVERITY_ERROR;
         case FATAL:
            return FacesMessage.SEVERITY_FATAL;
         case WARN:
            return FacesMessage.SEVERITY_WARN;
         case INFO:
         default:
            return FacesMessage.SEVERITY_INFO;
      }
   }

   /**
    * Calculate the JSF client ID from the provided widget ID.
    * TODO It would be great if this could do suffix maching.
    */
   private String getClientId(String targetId, FacesContext facesContext)
   {
      if (isAbsoluteClientIdPresent(targetId, facesContext))
      {
         return targetId;
      }
      else
      {
         return getClientId(facesContext.getViewRoot(), targetId, facesContext);
      }
   }

   /**
    * FIXME does not work if element is inside of form with prependId="false"
    */
   private boolean isAbsoluteClientIdPresent(String targetId, FacesContext facesContext)
   {
      return facesContext.getViewRoot().findComponent(targetId) != null;
   }

   private String getClientId(UIComponent component, String targetLocalId, FacesContext facesContext)
   {
      String currentLocalId = component.getId();
      if (currentLocalId != null && currentLocalId.equals(targetLocalId))
      {
         return component.getClientId(facesContext);
      }
      else
      {
         Iterator<UIComponent> iter = component.getFacetsAndChildren();
         while (iter.hasNext())
         {
            String clientId = getClientId(iter.next(), targetLocalId, facesContext);
            if (clientId != null)
            {
               return clientId;
            }
         }
         return null;
      }
   }
   
}
