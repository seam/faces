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
package org.jboss.seam.faces.el;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.jboss.seam.faces.lifecycle.ImportNamespacesProcessor;

/**
 * <p>
 * An {@link ELResolver} implementation that adds magic properties to a JSF
 * DataModel object.
 * </p>
 * 
 * <p>
 * The following is a list of the magic properties:
 * </p>
 * 
 * <ul>
 * <li>size - the size of the wrapped data</li>
 * <li>empty - a boolean indicating whether the wrapped data is empty</li>
 * </ul>
 * 
 * <p>
 * Assuming the variable <code>results</code> held a reference to a JSF
 * DataModel, you could print out its size using the following expression:
 * </p>
 * 
 * <pre>
 * #{results.size}
 * </pre>
 * 
 * @author Gavin King
 * @author Dan Allen
 */
public class SeamFacesELResolver
{
   public Object getValue(ELContext context, Object base, Object property)
   {
      if (base == null)
      {
         // we should be landing here after the JSR-299 resolver has a chance
         return resolveBase(context, property);
      }
      else if (base instanceof DataModel)
      {
         return resolveInDataModel(context, base, property);
      }

      return null;
   }

   public boolean isReadOnly(ELContext context, Object base, Object property)
   {
      return (base instanceof DataModel);
   }

   private Object resolveBase(ELContext context, Object property)
   {
      // is this check necessary?
      if (!(property instanceof String))
      {
         return null;
      }
      
      // FIXME refactor me to somewhere clean
      Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap(false);
      if (viewMap == null || !viewMap.containsKey(ImportNamespacesProcessor.NAMESPACES_CACHE_KEY))
      {
         return null;
      }
      
//      String name = (String) property;
//      BeanManager manager = ManagerBridge.getProvider().getCurrentManager();
//      for (String namespace : (Collection<String>) viewMap.get(ImportNamespacesProcessor.NAMESPACES_CACHE_KEY))
//      {
//         Set<Bean<?>> beans = manager.getBeans(namespace + "." + name);
//         // TODO complain if it is more than one
//         if (beans.size() == 1)
//         {
//            context.setPropertyResolved(true);
//            Bean<?> bean = beans.iterator().next();
//            return manager.getReference(bean, Object.class, manager.createCreationalContext(bean));
//         }
//      }
      
      return null;
   }
   
   private Object resolveInDataModel(ELContext context, Object base, Object property)
   {
      if ("size".equals(property))
      {
         context.setPropertyResolved(true);
         return ((DataModel<?>) base).getRowCount();
      }
      else if ("empty".equals(property))
      {
         context.setPropertyResolved(true);
         return ((DataModel<?>) base).getRowCount() == 0;
      }
      else
      {
         return null;
      }
   }
}
