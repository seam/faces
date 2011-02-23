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
