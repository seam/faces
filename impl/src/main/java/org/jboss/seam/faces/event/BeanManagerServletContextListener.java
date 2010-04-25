package org.jboss.seam.faces.event;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BeanManagerServletContextListener implements ServletContextListener
{
   @Inject 
   private BeanManager beanManager;

   public void contextDestroyed(ServletContextEvent sce)
   {
   }

   public void contextInitialized(ServletContextEvent sce)
   {
      sce.getServletContext().setAttribute(BeanManager.class.getName(), beanManager);
   }
   

}
