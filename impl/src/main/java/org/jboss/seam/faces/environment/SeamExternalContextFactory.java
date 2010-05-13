package org.jboss.seam.faces.environment;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextFactory;

public class SeamExternalContextFactory extends ExternalContextFactory
{
   private final ExternalContextFactory parent;

   public SeamExternalContextFactory(final ExternalContextFactory parent)
   {
      super();
      this.parent = parent;
   }

   @Override
   public ExternalContext getExternalContext(final Object context, final Object request, final Object response) throws FacesException
   {
      return new SeamExternalContext(getWrapped().getExternalContext(context, request, response));
   }

   @Override
   public ExternalContextFactory getWrapped()
   {
      return parent;
   }
}
