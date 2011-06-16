/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
