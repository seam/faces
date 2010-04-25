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
package org.jboss.seam.faces.beanManager;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.jboss.seam.faces.event.BeanManagerServletContextListener;
import org.jboss.weld.extensions.beanManager.BeanManagerProvider;

/**
 * A BeanManager provider for the Servlet Context attribute
 * "javax.enterprise.inject.spi.BeanManager"
 * 
 * @author Nicklas Karlsson
 * 
 */
public class FacesServletContextBeanManagerProvider implements BeanManagerProvider
{
   public static final FacesServletContextBeanManagerProvider DEFAULT = new FacesServletContextBeanManagerProvider();

   private static final List<String> SERVLET_CONTEXT_KEYS = new ArrayList<String>()
   {
      private static final long serialVersionUID = 1L;
      {
         add(BeanManager.class.getName());
         add(BeanManagerServletContextListener.BEANMANAGER_SERVLETCONTEXT_KEY);
      }
   };

   public BeanManager getBeanManager()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      if (facesContext == null)
      {
         return null;
      }
      ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
      return getBeanManager(servletContext);
   }

   private BeanManager getBeanManager(ServletContext servletContext)
   {
      BeanManager beanManager = null;
      for (String key : SERVLET_CONTEXT_KEYS)
      {
         beanManager = (BeanManager) servletContext.getAttribute(key);
         if (beanManager != null)
         {
            break;
         }
      }
      return beanManager;
   }

   public int getPrecedence()
   {
      return 20;
   }

}