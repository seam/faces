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

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;

/**
 * This class is registered with the JSF framework to invoke the <code>CatchExceptionHandler</code>
 * as part of the JSF lifecycle
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class CatchExceptionHandlerFactory extends ExceptionHandlerFactory 
{
	private transient final Logger log = Logger.getLogger(CatchExceptionHandlerFactory.class);
	
	private ExceptionHandlerFactory parent;
	
	private transient boolean catchUnavailable = false;
	
	public CatchExceptionHandlerFactory(ExceptionHandlerFactory parent) 
	{
		super();
		this.parent = parent;
	}

	@Override
	public ExceptionHandler getExceptionHandler() 
	{
        log.trace("Creating the JSF exception handler");
        if (catchUnavailable)
        {
           log.trace("Catch integration previously disabled");
           return parent.getExceptionHandler();
        }
        
        BeanManager beanManager = null;
        try
        {
           BeanManagerLocator beanManagerLocator = new BeanManagerLocator();
           beanManager = beanManagerLocator.getBeanManager();
        }
        catch (IllegalArgumentException e)
        {
           log.info("Could not location BeanManager, Catch integration disabled");
           catchUnavailable = true;
           return parent.getExceptionHandler();
        }
        
        if (beanManager.getBeans(CatchExceptionHandler.class).isEmpty())
        {
           log.info("Catch not available, Catch integration disabled");
           catchUnavailable = true;
           return parent.getExceptionHandler();
        }
        log.info("Catch integration enabled");
        return new CatchExceptionHandler(parent.getExceptionHandler(), beanManager);
	}	
}
