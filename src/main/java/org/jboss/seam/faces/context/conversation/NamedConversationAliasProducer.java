package org.jboss.seam.faces.context.conversation;

import javax.enterprise.context.Conversation;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.inject.Named;

/**
 * Exposes the {@link Conversation} under the simplified name "conversation"
 * in addition to the default "javax.enterprise.context.conversation". This
 * alias is provided for the page author's convenience.
 *
 * @author Dan Allen
 */
public class NamedConversationAliasProducer
{
   public @Produces @Named @Typed() Conversation getConversation(Conversation conversation)
   {
      return conversation;
   }
}
