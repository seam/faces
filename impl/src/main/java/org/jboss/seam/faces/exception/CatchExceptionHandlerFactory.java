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
package org.jboss.seam.faces.exception;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import org.jboss.solder.logging.Logger;
import org.jboss.solder.beanManager.BeanManagerLocator;
import org.jboss.solder.beanManager.BeanManagerUnavailableException;

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
