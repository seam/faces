package org.jboss.seam.faces.exception;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;
import org.jboss.seam.solder.beanManager.BeanManagerUnavailableException;

/**
 * This class is registered with the JSF framework to invoke the <code>CatchExceptionHandler</code> as part of the JSF lifecycle
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class CatchExceptionHandlerFactory extends ExceptionHandlerFactory {
    private transient final Logger log = Logger.getLogger(CatchExceptionHandlerFactory.class);

    private ExceptionHandlerFactory parent;

    private transient boolean catchUnavailable = false;

    private transient BeanManagerLocator locator;

    public CatchExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        super();
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        log.trace("Creating the JSF exception handler");
        if (catchUnavailable) {
            log.trace("Catch integration previously disabled");
            return parent.getExceptionHandler();
        }

        BeanManager beanManager = null;
        try {
            locator = new BeanManagerLocator();
            beanManager = locator.getBeanManager();
        } catch (BeanManagerUnavailableException e) {
            log.info("Could not location BeanManager, Catch integration disabled");
            catchUnavailable = true;
            return parent.getExceptionHandler();
        }

        // TODO this looks like a nice utility for Solder
        if (beanManager.getBeans(CatchExceptionHandler.class).isEmpty()) {
            log.info("Catch not available, Catch integration disabled");
            catchUnavailable = true;
            return parent.getExceptionHandler();
        }

        if (log.isTraceEnabled()) {
            log.trace("Catch integration enabled");
        }
        return new CatchExceptionHandler(parent.getExceptionHandler(), beanManager);
    }
}
