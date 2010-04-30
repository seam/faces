package org.jboss.seam.faces.display;

import java.util.Set;

/**
 * A convenient way to add messages to be displayed to the user as Feedback
 * messages, Toast, Alerts, etc...
 * 
 * Messages can be displayed by using the <code>&lt;h:messages /&gt;</code> tag
 * in any given View.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface Messages
{
   /**
    * Create a new {@link Message} object and add it to the pending message
    * cache. Messages remain pending until the Render Response phase is next
    * invoked. E.g: If a redirect is issued before Render Response occurs,
    * messages will be displayed during the next Render Response phase unless
    * {@link Messages#clear()} is called, or the user's Session expires.
    * <p>
    * <b>Note:</b> Duplicate messages are ignored.
    */
   Message add(Level level);

   /**
    * Retrieve all pending messages.
    */
   Set<Message> getAll();

   /**
    * Clears all pending messages.
    */
   void clear();
}