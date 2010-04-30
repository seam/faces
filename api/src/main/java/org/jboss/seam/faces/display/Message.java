package org.jboss.seam.faces.display;

import java.io.Serializable;

/**
 * A convenient message to be displayed to the user as Feedback, Toast, Alerts,
 * etc... See {@link Messages} for usage.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface Message extends Serializable
{
   /**
    * Set the message summary to be displayed if the output component is enabled
    * to display summary. This is the primary text of the message.
    * 
    * @param message
    * @return The builder pattern {@link Message}
    */
   Message summary(String message);

   /**
    * Set the message details to be displayed if the output component is enabled
    * to display detail. This is the secondary text of the message.
    * 
    * @return The builder pattern {@link Message}
    */
   Message details(String details);

   /**
    * Specifies that this message should be displayed by the corresponding
    * <code>&lt;h:message for="clientId" /&gt;</code> tag, where clientId is the
    * ID of the component to which this message belongs.
    * 
    * @param The clientId of the component to which this message should be
    *           attached/displayed
    */
   Message component(String clientId);

   public Level getLevel();

   public String getMessage();

   public String getDetails();

   public String getClientId();
}