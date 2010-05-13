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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.faces.view.facelets.ResourceResolver;

import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class SeamResourceResolverTest
{
   SeamResourceResolver resolver = new SeamResourceResolver(new ResourceResolver()
   {
      @Override
      public URL resolveUrl(final String path)
      {
         return null;
      }
   });

   @Test
   public void testResolveClasspathUrl()
   {
      String validPath = "/org/jboss/seam/faces/environment/mock.resource";
      URL url = resolver.resolveUrl(validPath);
      assertTrue(url instanceof URL);
   }

   @Test
   public void testResolveUnprefixedClasspathUrl()
   {
      String validPath = "org/jboss/seam/faces/environment/mock.resource";
      URL url = resolver.resolveUrl(validPath);
      assertTrue(url instanceof URL);
   }

   @Test
   public void testNullInputYieldsNullOutput() throws Exception
   {
      URL url = resolver.resolveUrl(null);
      assertNull(url);
   }

   @Test
   public void testResolveUrlReturnsNullIfNotFound()
   {
      String invalidPath = "some/nonexistent/mock.resource";
      URL url = resolver.resolveUrl(invalidPath);
      assertNull(url);
   }

}
