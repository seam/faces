package org.jboss.seam.faces.test.environment;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.environment.FacesContextProducer;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verify that the FacesContextProducer produces the same FacesContext as
 * returned by FacesContext#getCurrentInstance() and by the CDI producer method.
 * 
 * @author Dan Allen
 */
@RunWith(Arquillian.class)
public class FacesContextProducerTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      return ShrinkWrap.create(JavaArchive.class).addClass(FacesContextProducer.class).addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
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
      // Disabled as hashCode is not passed through the proxy in weld
      // 1.1.0.Beta1
      // Assert.assertEquals(actualFacesContext.hashCode(),
      // producedFacesContext.hashCode());

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
