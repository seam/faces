/**
 * 
 */
package org.jboss.seam.faces;

import javax.enterprise.context.Conversation;
import javax.inject.Singleton;

/**
 * Provide a mocked conversation object for use in Unit tests. This entire class
 * is a no-op; it does <i>nothing</i>.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
public class MockConversation implements Conversation
{
   private long timeout;
   private String id;
   private boolean persistent;

   public void begin()
   {
      this.id = "generated";
      persistent = true;
   }

   public void begin(final String id)
   {
      this.id = id;
      persistent = true;
   }

   public void end()
   {
      persistent = false;
   }

   public String getId()
   {
      return id;
   }

   public long getTimeout()
   {
      return timeout;
   }

   public boolean isTransient()
   {
      return this.persistent == false;
   }

   public void setTimeout(final long milliseconds)
   {
      this.timeout = milliseconds;
   }

}
