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

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

/**
 * Singleton(ish) extension that observes the AfterBeanDiscovery event and stores the BeanManager for access
 * in places where injection is not available and JNDI or ServletContext access is not preferable.
 * 
 * @author Nicklas Karlsson
 * 
 */
public class BeanManagerPickupExtension implements Extension
{
   private static BeanManagerPickupExtension instance;
   private volatile BeanManager beanManager;

   public BeanManager getBeanManager()
   {
      return beanManager;
   }

   public static BeanManagerPickupExtension getInstance()
   {
      return instance;
   }

   public void pickupBeanManager(@Observes AfterBeanDiscovery e, BeanManager beanManager)
   {
      this.beanManager = beanManager;
      BeanManagerPickupExtension.instance = this;
   }
}
