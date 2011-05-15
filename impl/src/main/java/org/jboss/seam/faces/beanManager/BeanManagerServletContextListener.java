package org.jboss.seam.faces.beanManager;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jboss.seam.solder.beanManager.BeanManagerLocator;

/**
 * A Servlet Context Listener that places the BeanManager under a known attribute key
 *
 * @author Nicklas Karlsson
 */
public class BeanManagerServletContextListener implements ServletContextListener {

    public static final String BEANMANAGER_SERVLETCONTEXT_KEY = "org.jboss.seam.faces.javax.enterprise.spi.BeanManager";

    @Inject
    private BeanManager beanManager;

    public void contextDestroyed(ServletContextEvent sce) {
    }

    public void contextInitialized(ServletContextEvent sce) {

        // servlet context attribute supported by Weld 1.0.x and OpenWebBeans
        if (beanManager == null) {
            beanManager = (BeanManager) sce.getServletContext().getAttribute(BeanManager.class.getName());
        }

        // servlet context attribute supported by Weld 1.1.x
        if (beanManager == null) {
            beanManager = (BeanManager) sce.getServletContext().getAttribute(
                    "org.jboss.weld.environment.servlet.javax.enterprise.inject.spi.BeanManager");
        }

        // lookup via BeanManagerLocator
        if (beanManager == null) {
            BeanManagerLocator locator = new BeanManagerLocator();
            if (locator.isBeanManagerAvailable()) {
                beanManager = locator.getBeanManager();
            }
        }

        // place BeanManager in the known attribute
        sce.getServletContext().setAttribute(BEANMANAGER_SERVLETCONTEXT_KEY, beanManager);

    }

}
