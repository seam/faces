package org.jboss.seam.faces.environment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.inject.Inject;

import org.jboss.seam.faces.context.FlashContext;
import org.jboss.seam.faces.context.FlashScopedContext;

@RequestScoped
public class SeamExternalContext extends ExternalContextWrapper
{
   private ExternalContext wrapped;

   @Inject
   FlashContext flash;

   @Inject
   FlashScopedContext context;

   public void setWrapped(final ExternalContext wrapped)
   {
      this.wrapped = wrapped;
   }

   @Override
   public ExternalContext getWrapped()
   {
      return wrapped;
   }

   @Override
   public String encodeRedirectURL(final String baseUrl, Map<String, List<String>> parameters)
   {
      if (!flash.isEmpty())
      {
         if (parameters == null)
         {
            parameters = new HashMap<String, List<String>>();
         }
         parameters.put(context.getRequestParameterName(), Arrays.asList(flash.getId()));
      }
      return super.encodeRedirectURL(baseUrl, parameters);
   }
}
