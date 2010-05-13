package org.jboss.seam.faces.environment;

import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.faces.view.facelets.ResourceResolver;

public class SeamExternalContext extends ExternalContextWrapper
{
   private final ExternalContext wrapped;
   private final ResourceResolver resolver = new SeamResourceResolver();

   public SeamExternalContext(final ExternalContext wrapped)
   {
      this.wrapped = wrapped;
   }

   @Override
   public URL getResource(final String path) throws MalformedURLException
   {
      URL url = resolver.resolveUrl(path);
      if (url == null)
      {
         url = getWrapped().getResource(path);
      }
      return url;
   }

   @Override
   public ExternalContext getWrapped()
   {
      return wrapped;
   }
}
