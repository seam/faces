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
package org.jboss.seam.faces.test.environment;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.inject.Instance;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.environment.ExternalContextProducer;
import org.jboss.seam.faces.environment.FacesContextProducer;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verify that the ExternalContextProducer produces the same ExternalContext as returned by
 * FacesContext#getExternalContext() and the CDI producer method.
 * 
 * @author Dan Allen
 */
@RunWith(Arquillian.class)
public class ExternalContextProducerTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      return ShrinkWrap.create(JavaArchive.class).addClass(FacesContextProducer.class).addClass(
               ExternalContextProducer.class).addManifestResource(new ByteArrayAsset(new byte[0]),
               ArchivePaths.create("beans.xml"));
   }

   @Inject
   Instance<ExternalContext> externalContextInstance;

   @Test
   public void testReturnsCurrentExternalContext()
   {
      new MockFacesContext().set();
      FacesContext ctx = FacesContext.getCurrentInstance();
      Assert.assertSame(new ExternalContextProducer().getExternalContext(ctx), ctx.getExternalContext());
   }

   @Test
   public void testProducesContextualCurrentFacesContext()
   {
      new MockFacesContext().set();

      ExternalContext actualExternalContext = FacesContext.getCurrentInstance().getExternalContext();
      ExternalContext producedExternalContext = externalContextInstance.get();

      // not equal since the produced ExternalContext is a proxy
      Assert.assertFalse(actualExternalContext == producedExternalContext);
      // verify we have same object through proxy by comparing hash codes

      // Disabled as hashCode is not passed through the proxy in weld
      // 1.1.0.Beta1
      // Assert.assertEquals(actualExternalContext.hashCode(),
      // producedExternalContext.hashCode());

      // Assert.assertEquals(actualExternalContext, producedExternalContext);
      Assert.assertEquals("/app", producedExternalContext.getRequestContextPath());
   }

   @Test(expected = ContextNotActiveException.class)
   public void testProducerThrowsExceptionWhenFacesContextNotActive()
   {
      new MockFacesContext().release();
      // NOTE the return value must be invoked to carry out the lookup
      externalContextInstance.get().toString();
   }
}
