package org.jboss.seam.faces.environment;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

import org.jboss.seam.faces.util.BeanManagerUtils;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;

/**
 * Implementation of {@link ApplicationFactory} creating {@link SeamApplicationWrapper} instances that wrap the
 * {@link Application} created by the underlying factory.
 * 
 * @author <a href="mailto:christian@kaltepoth.de">Christian Kaltepoth</a>
 */
public class SeamApplicationFactory extends ApplicationFactory {

    private final ApplicationFactory delegate;

    private SeamApplicationWrapper applicationWrapper;

    public SeamApplicationFactory(ApplicationFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public Application getApplication() {

        // retrieve SeamApplicationWrapper and set parent correctly
        if (applicationWrapper == null) {
            BeanManager beanManager = new BeanManagerLocator().getBeanManager();
            applicationWrapper = BeanManagerUtils.getContextualInstance(beanManager, SeamApplicationWrapper.class);
            applicationWrapper.setParent(delegate.getApplication());
        }

        return applicationWrapper;
    }

    @Override
    public void setApplication(Application application) {
        
        delegate.setApplication(application);

        // reset wrapper so it gets recreated when getApplication() is called
        applicationWrapper = null;

    }

}
