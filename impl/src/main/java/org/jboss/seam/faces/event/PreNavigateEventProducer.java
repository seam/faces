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

import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import org.jboss.weld.extensions.beanManager.BeanManagerAccessor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class PreNavigateEventProducer extends ConfigurableNavigationHandler
{
   private final ConfigurableNavigationHandler parent;

   public PreNavigateEventProducer(final ConfigurableNavigationHandler parent)
   {
      this.parent = parent;
   }

   @Override
   public NavigationCase getNavigationCase(final FacesContext context, final String fromAction, final String outcome)
   {
      return parent.getNavigationCase(context, fromAction, outcome);
   }

   @Override
   public Map<String, Set<NavigationCase>> getNavigationCases()
   {
      return parent.getNavigationCases();
   }

   @Override
   public void handleNavigation(final FacesContext context, final String fromAction, final String outcome)
   {
      BeanManager manager = BeanManagerAccessor.getBeanManager();
      NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();

      NavigationCase navigationCase;
      if (navigationHandler instanceof ConfigurableNavigationHandler)
      {
         navigationCase = ((ConfigurableNavigationHandler) navigationHandler).getNavigationCase(context, fromAction, outcome);
      }
      else
      {
         navigationCase = getNavigationCase(context, fromAction, outcome);
      }
      manager.fireEvent(new PreNavigateEvent(context, fromAction, outcome, navigationCase));
      parent.handleNavigation(context, fromAction, outcome);
   }

}
