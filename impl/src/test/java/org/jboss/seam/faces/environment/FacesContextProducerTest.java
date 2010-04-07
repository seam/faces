/*
 * JBoss, Community-driven Open Source Middleware
 * Copyright 2010, JBoss by Red Hat, Inc., and individual contributors
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

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verify that the FacesContextProducer produces the same FacesContext as
 * returned by FacesContext#getCurrentInstance() and by the CDI producer
 * method.
 * 
 * @author Dan Allen
 */
@RunWith(Arquillian.class)
public class FacesContextProducerTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClass(FacesContextProducer.class).addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject
   Instance<FacesContext> facesContextInstance;

   @Test
   public void testReturnsCurrentFacesContext()
   {
      new MockFacesContext().set();
      Assert.assertSame(new FacesContextProducer().getFacesContext(), FacesContext.getCurrentInstance());
   }

   @Test
   public void testProducesContextualCurrentFacesContext()
   {
      new MockFacesContext().set().setCurrentPhaseId(PhaseId.RENDER_RESPONSE);

      FacesContext actualFacesContext = FacesContext.getCurrentInstance();
      FacesContext producedFacesContext = facesContextInstance.get();

      // not equal since the produced FacesContext is a proxy
      Assert.assertFalse(actualFacesContext == producedFacesContext);
      // verify we have same object through proxy by comparing hash codes
      Assert.assertEquals(actualFacesContext.hashCode(), producedFacesContext.hashCode());
      // Assert.assertEquals(actualFacesContext, producedFacesContext);
      Assert.assertSame(PhaseId.RENDER_RESPONSE, producedFacesContext.getCurrentPhaseId());
   }

   @Test(expected = ContextNotActiveException.class)
   public void testProducerThrowsExceptionWhenFacesContextNotActive()
   {
      new MockFacesContext().release();
      // NOTE the return value must be invoked to carry out the lookup
      facesContextInstance.get().toString();
   }
}
