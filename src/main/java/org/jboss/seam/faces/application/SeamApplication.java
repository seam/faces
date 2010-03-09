/* 
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
 *
 * $Id$
 */
package org.jboss.seam.faces.application;

import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;

/**
 * Proxies the JSF Application object, and adds all kinds
 * of tasty extras.
 *
 * @author Gavin King
 */
public class SeamApplication extends ApplicationWrapper {

	protected Application delegate;

	public SeamApplication(Application delegate)
	{
		this.delegate = delegate;
	}

	@Override
	public Application getWrapped() {
		return delegate;
	}

//	@Override
//	public ExpressionFactory getExpressionFactory() {
//      // TODO need to push SeamFacesELResolver into SeamEL composite resolver
//		return SeamExpressionFactory.INSTANCE;
//	}

}
