/**
 * 
 */
package org.jboss.seam.faces.context.conversation;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.seam.faces.SeamFacesException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public @RequestScoped class ConversationalBean
{
   @Inject
   Conversation conversation;

   private boolean conversationLongRunningDuringInvocation;
   private boolean conversationLongRunningDuringInvocation2;
   private boolean conversationLongRunningDuringInvocation3;
   private boolean conversationLongRunningDuringInvocation4;
   private boolean conversationLongRunningDuringInvocation5;

   private boolean conversationLongRunningDuringInvocation6;

   @Begin
   public void begin()
   {
   }

   @Begin(timeout = 1000)
   public void beginConversation()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation = true;
      }
   }

   @Begin
   @End
   public void beginAndEndConversation()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation2 = true;
      }
   }

   @Begin(permit = { SeamFacesException.class })
   public void beginAndThrowFatalException()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation3 = true;
      }
      throw new RuntimeException("A vanilla exception.");
   }

   @Begin(permit = { SeamFacesException.class })
   public void beginAndThrowPermittedException()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation4 = true;
      }
      throw new SeamFacesException("Just so it's not a vanilla Exception.");
   }

   @End(permit = { SeamFacesException.class })
   public void endAndThrowFatalException()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation5 = true;
      }
      throw new RuntimeException("A vanilla exception.");
   }

   @End(permit = { SeamFacesException.class })
   public void endAndThrowPermittedException()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation6 = true;
      }
      throw new SeamFacesException("A vanilla exception.");
   }

   public boolean isConversationLongRunningInsideMethodCall()
   {
      return conversationLongRunningDuringInvocation;
   }

   public boolean isConversationLongRunningDuringInvocation2()
   {
      return conversationLongRunningDuringInvocation2;
   }

   public boolean isConversationLongRunningDuringInvocation3()
   {
      return conversationLongRunningDuringInvocation3;
   }

   public boolean isConversationLongRunningDuringInvocation4()
   {
      return conversationLongRunningDuringInvocation4;
   }

   public boolean isConversationLongRunningDuringInvocation5()
   {
      return conversationLongRunningDuringInvocation5;
   }

   public boolean isConversationLongRunningDuringInvocation6()
   {
      return conversationLongRunningDuringInvocation6;
   }

}
