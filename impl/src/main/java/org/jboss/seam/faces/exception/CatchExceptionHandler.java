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

import java.text.MessageFormat;
import java.util.Iterator;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;

import org.jboss.logging.Logger;
import org.jboss.seam.exception.control.ExceptionToCatch;
import org.jboss.seam.faces.qualifier.FacesLiteral;
import org.jboss.seam.solder.core.Requires;

/**
 * This class fires Exceptions from the JSF lifecycle into Seam Catch.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@Requires("org.jboss.seam.exception.control.extension.CatchExtension")
public class CatchExceptionHandler extends ExceptionHandlerWrapper {
    private transient final Logger log = Logger.getLogger(CatchExceptionHandler.class);

    private ExceptionHandler wrapped;
    private BeanManager beanManager;

    // default constructor required to make it a bean
    public CatchExceptionHandler() {
    }

    public CatchExceptionHandler(ExceptionHandler wrapped, BeanManager beanManager) {
        this.wrapped = wrapped;
        this.beanManager = beanManager;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        log.trace("Handling Exceptions");
        for (Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator(); it.hasNext();) {
            Throwable t = it.next().getContext().getException();
            log.trace(MessageFormat.format("Handling Exception {0}", t.getClass().getName()));
            if (!(t instanceof AbortProcessingException)) // Why is this needed
            {
                ExceptionToCatch catchEvent = new ExceptionToCatch(t, FacesLiteral.INSTANCE);
                try {
                    if (log.isTraceEnabled()) {
                        log.trace("Firing event");
                    }
                    beanManager.fireEvent(catchEvent);
                } catch (Exception e) {
                    if (!e.equals(t)) {
                        log.debug("Throwing exception thrown from within Seam Catch");
                        throw new RuntimeException(e);
                    }
                    continue; // exception will be handled by getWrapped().handle() below
                }
                if (catchEvent.isHandled()) {
                    log.debug(MessageFormat.format("Exception handled {0}", t.getClass().getName()));
                    it.remove();
                }
            }
        }
        if (getUnhandledExceptionQueuedEvents().iterator().hasNext()) {
            log.debug("Exceptions remain, will be thrown outside of JSF");
            getWrapped().handle();
        }
    }
}
