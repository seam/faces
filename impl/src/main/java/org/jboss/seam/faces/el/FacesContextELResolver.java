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

package org.jboss.seam.faces.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;

import org.jboss.weld.extensions.el.Resolver;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Resolver
public class FacesContextELResolver extends ELResolver
{
   private ELResolver getWrapped()
   {
      return FacesContext.getCurrentInstance().getELContext().getELResolver();
   }

   @Override
   public Object getValue(ELContext context, Object base, Object property)
   {
      return getWrapped().getValue(context, base, property);
   }

   @Override
   public Class<?> getType(ELContext context, Object base, Object property)
   {
      return getWrapped().getType(context, base, property);
   }

   @Override
   public void setValue(ELContext context, Object base, Object property, Object value)
   {
      getWrapped().setValue(context, base, property, value);
   }

   @Override
   public boolean isReadOnly(ELContext context, Object base, Object property)
   {
      return getWrapped().isReadOnly(context, base, property);
   }

   @Override
   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
   {
      return getWrapped().getFeatureDescriptors(context, base);
   }

   @Override
   public Class<?> getCommonPropertyType(ELContext context, Object base)
   {
      return getWrapped().getCommonPropertyType(context, base);
   }

}
