package org.jboss.seam.faces.event;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class GenericEventListener
{
   @Inject
   BeanManager beanManager;

   protected BeanManager getBeanManager()
   {
      if (beanManager == null)
      {
         beanManager = lookupBeanManager();
      }
      return beanManager;
   }

   private BeanManager lookupBeanManager()
   {
      try
      {
         return beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
      }
      catch (NamingException e)
      {
         throw new IllegalArgumentException("Could not find BeanManager in JNDI", e);
      }
   }
}
