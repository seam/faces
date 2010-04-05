/**
 * 
 */
package org.jboss.seam.faces.examples;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named
@RequestScoped
public class RedirectBean
{
   @Inject
   FacesContext context;

   private String key;

   public void send() throws IOException
   {
      String url = "http://ocpsoft.com";
      System.out.println("Sent redirect for key: " + key);
      HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
      response.sendRedirect(url);
      context.responseComplete();
   }

   public String getKey()
   {
      return key;
   }

   public void setKey(final String key)
   {
      this.key = key;
   }
}
