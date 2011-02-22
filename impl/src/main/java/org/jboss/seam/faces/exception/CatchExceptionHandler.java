/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
import org.jboss.seam.faces.literal.FacesLiteral;
import org.jboss.seam.solder.core.Requires;

/**
 * This class fires Exceptions from the JSF lifecycle into Seam Catch.
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@Requires("org.jboss.seam.exception.control.extension.CatchExtension")
public class CatchExceptionHandler extends ExceptionHandlerWrapper
{
    private transient final Logger log = Logger.getLogger(CatchExceptionHandler.class);

    private ExceptionHandler wrapped;
    private BeanManager beanManager;

    // default constructor required to make it a bean
    public CatchExceptionHandler()
    {
    }

    public CatchExceptionHandler(ExceptionHandler wrapped, BeanManager beanManager)
    {
        this.wrapped = wrapped;
        this.beanManager = beanManager;
    }

    @Override
    public ExceptionHandler getWrapped()
    {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException
    {
        log.trace("Handling Exceptions");
        for (Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator(); it.hasNext();)
        {
            Throwable t = it.next().getContext().getException();
            log.trace(MessageFormat.format("Handling Exception {0}", t.getClass().getName()));
            if (!(t instanceof AbortProcessingException)) // Why is this needed
            {
                ExceptionToCatch catchEvent = new ExceptionToCatch(t, FacesLiteral.INSTANCE);
                try
                {
                    log.trace("Firing event");
                    beanManager.fireEvent(catchEvent);
                }
                catch (Exception e)
                {
                    continue;  // exception will be handled by getWrapped().handle() below
                }
                if (catchEvent.isHandled())
                {
                    log.debug(MessageFormat.format("Exception handled {0}", t.getClass().getName()));
                    it.remove();
                }
            }
        }
        if (getUnhandledExceptionQueuedEvents().iterator().hasNext())
        {
            log.debug("Exceptions remain, will be thrown outside of JSF");
            getWrapped().handle();
        }
    }
}
