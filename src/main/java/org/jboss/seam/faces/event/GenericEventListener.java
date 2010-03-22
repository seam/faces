package org.jboss.seam.faces.event;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@SuppressWarnings("serial")
public class GenericEventListener
{

   @Inject
   BeanManager beanManager;

   // FIXME: hack to work around invalid binding in JBoss AS 6 M2
   private static final List<String> beanManagerLocations = new ArrayList<String>()
   {
      {
         add("java:comp/BeanManager");
         add("java:app/BeanManager");
      }
   };

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
      for (String location : beanManagerLocations)
      {
         try
         {
            return (BeanManager) new InitialContext().lookup(location);
         }
         catch (NamingException e)
         {
            // No panic, keep trying
         }
      }
      throw new IllegalArgumentException("Could not find BeanManager in " + beanManagerLocations);
   }
}
