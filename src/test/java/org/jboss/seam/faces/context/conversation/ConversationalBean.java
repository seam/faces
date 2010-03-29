/**
 * 
 */
package org.jboss.seam.faces.context.conversation;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RequestScoped
public class ConversationalBean
{
   @Inject
   Conversation conversation;

   private boolean conversationLongRunningDuringInvocation = false;

   private boolean conversationLongRunningDuringInvocation2;

   @Begin
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
      conversationLongRunningDuringInvocation2 = true;
   }

   public boolean isConversationLongRunningDuringInvocation2()
   {
      return conversationLongRunningDuringInvocation2;
   }

   public void setConversationLongRunningDuringInvocation2(final boolean conversationLongRunningDuringInvocation2)
   {
      this.conversationLongRunningDuringInvocation2 = conversationLongRunningDuringInvocation2;
   }

   public boolean isConversationLongRunningInsideMethodCall()
   {
      return conversationLongRunningDuringInvocation;
   }

}
