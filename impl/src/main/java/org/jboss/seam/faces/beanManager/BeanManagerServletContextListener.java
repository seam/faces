package org.jboss.seam.faces.beanManager;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A Servlet Context Listener that places the BeanManager under a known attribute key
 * 
 * @author Nicklas Karlsson
 *
 */
public class BeanManagerServletContextListener implements ServletContextListener
{
   public static final String BEANMANAGER_SERVLETCONTEXT_KEY = "org.jboss.seam.faces.javax.enterprise.spi.BeanManager"; 
   
   @Inject 
   private BeanManager beanManager;

   public void contextDestroyed(ServletContextEvent sce)
   {
   }

   public void contextInitialized(ServletContextEvent sce)
   {
      sce.getServletContext().setAttribute(BEANMANAGER_SERVLETCONTEXT_KEY, beanManager);
   }
   

}
