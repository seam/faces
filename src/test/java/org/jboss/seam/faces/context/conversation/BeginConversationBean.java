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
public class BeginConversationBean
{
   @Inject Conversation conversation;

   private boolean conversationLongRunningDuringInvocation = false;

   @Begin
   public void beginConversation()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation = true;
      }
   }

   public boolean isConversationLongRunningInsideMethodCall() {
      return conversationLongRunningDuringInvocation;
   }

}
