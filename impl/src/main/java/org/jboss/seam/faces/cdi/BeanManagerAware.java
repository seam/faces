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
package org.jboss.seam.faces.cdi;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 
 * @author Nicklas Karlsson
 *
 */
public class BeanManagerAware
{
   @Inject
   BeanManager beanManager;

   // FIXME: hack to work around invalid binding in JBoss AS 6 M2
   private static final List<String> beanManagerLocations;

   static
   {
      beanManagerLocations = new ArrayList<String>();
      beanManagerLocations.add("java:comp/BeanManager");
      beanManagerLocations.add("java:app/BeanManager");
   }

   protected BeanManager getBeanManager()
   {
      if (beanManager == null)
      {
         beanManager = lookupBeanManager();
      }
      return beanManager;
   }

   private BeanManager lookupBeanManager()
   {
      for (String location : beanManagerLocations)
      {
         try
         {
            return (BeanManager) new InitialContext().lookup(location);
         }
         catch (NamingException e)
         {
            // No panic, keep trying
         }
      }
      throw new IllegalArgumentException("Could not find BeanManager in " + beanManagerLocations);
   }
}
