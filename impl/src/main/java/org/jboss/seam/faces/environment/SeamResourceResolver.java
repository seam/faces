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

package org.jboss.seam.faces.environment;

import java.net.URL;

import javax.faces.view.facelets.ResourceResolver;

/**
 * Allow resolution of classpath resources.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class SeamResourceResolver extends ResourceResolver
{
   private final ResourceResolver parent;

   public SeamResourceResolver()
   {
      this.parent = null;
   }

   public SeamResourceResolver(final ResourceResolver parent)
   {
      this.parent = parent;
   }

   @Override
   public URL resolveUrl(final String path)
   {
      URL result = null;
      if (path != null)
      {
         String canonicalPath = path;
         if (path.startsWith("/"))
         {
            canonicalPath = path.substring(1);
         }

         result = Thread.currentThread().getContextClassLoader().getResource(canonicalPath);
         if ((result == null) && (parent != null))
         {
            result = parent.resolveUrl(path);
         }
      }
      return result;
   }
}
