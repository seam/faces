/**
 * 
 */
package org.jboss.seam.faces.context.conversation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@ApplicationScoped
public class BeginConversationBean
{
   @Inject
   Conversation conversation;

   public boolean conversationStarted = false;

   @Begin
   public void beginConversation()
   {
      if (conversation.isTransient() == false)
      {
         conversationStarted = true;
      }
   }
}
