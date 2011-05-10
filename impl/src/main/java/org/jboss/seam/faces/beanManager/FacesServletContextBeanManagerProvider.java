package org.jboss.seam.faces.beanManager;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.jboss.seam.solder.beanManager.BeanManagerProvider;

/**
 * A BeanManager provider for the Servlet Context attribute "javax.enterprise.inject.spi.BeanManager"
 *
 * @author Nicklas Karlsson
 */
public class FacesServletContextBeanManagerProvider implements BeanManagerProvider {
    public static final FacesServletContextBeanManagerProvider DEFAULT = new FacesServletContextBeanManagerProvider();

    private static final List<String> SERVLET_CONTEXT_KEYS = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add(BeanManager.class.getName());
            add(BeanManagerServletContextListener.BEANMANAGER_SERVLETCONTEXT_KEY);
        }
    };

    public BeanManager getBeanManager() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return null;
        }
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        return getBeanManager(servletContext);
    }

    private BeanManager getBeanManager(ServletContext servletContext) {
        BeanManager beanManager = null;
        for (String key : SERVLET_CONTEXT_KEYS) {
            beanManager = (BeanManager) servletContext.getAttribute(key);
            if (beanManager != null) {
                break;
            }
        }
        return beanManager;
    }

    public int getPrecedence() {
        return 20;
    }

}
