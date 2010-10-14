package org.jboss.seam.faces.environment;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextFactory;

import org.jboss.seam.faces.util.BeanManagerUtils;
import org.jboss.weld.extensions.beanManager.BeanManagerAccessor;

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
      try
      {
         BeanManager manager = BeanManagerAccessor.getBeanManager();

         SeamExternalContext seamExternalContext = BeanManagerUtils.getContextualInstance(manager, SeamExternalContext.class);
         seamExternalContext.setWrapped(parent.getExternalContext(context, request, response));

         return seamExternalContext;
      }
      catch (Exception e)
      {
         throw new IllegalStateException("Could not wrap ExternalContext", e);
      }
   }

   @Override
   public ExternalContextFactory getWrapped()
   {
      return parent;
   }
}
