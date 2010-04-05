/**
 * 
 */
package org.jboss.seam.faces.examples;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.faces.context.conversation.End;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named
@ConversationScoped
public class LinkBean implements Serializable
{
   private static final long serialVersionUID = -2209547152337410725L;

   private String key;
   private String url;

   @Begin
   @End
   public String createLink()
   {
      System.out.println("Created link: [ " + key + " => " + url + " ]");
      return "pretty:edit";
   }

   public String getKey()
   {
      return key;
   }

   public void setKey(final String key)
   {
      this.key = key;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(final String url)
   {
      this.url = url;
   }
}
