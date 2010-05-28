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

package org.jboss.seam.faces.event;

import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

/**
 * An event that is fired when JSF navigation is invoked via the
 * {@link NavigationHandler}, but before any redirecting or non-redirecting
 * navigation is completed, giving consumers of this event a chance to take
 * action.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class PreNavigateEvent
{

   private final FacesContext context;
   private final String fromAction;
   private final String outcome;
   private final NavigationCase navigationCase;

   public PreNavigateEvent(final FacesContext context, final String fromAction, final String outcome, final NavigationCase navigationCase)
   {
      this.context = context;
      this.fromAction = fromAction;
      this.outcome = outcome;
      this.navigationCase = navigationCase;
   }

   public FacesContext getContext()
   {
      return context;
   }

   public String getFromAction()
   {
      return fromAction;
   }

   public String getOutcome()
   {
      return outcome;
   }

   public NavigationCase getNavigationCase()
   {
      return navigationCase;
   }

}
