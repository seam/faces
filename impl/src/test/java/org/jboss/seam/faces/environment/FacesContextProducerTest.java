package org.jboss.seam.faces.environment;

import java.util.Iterator;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.inject.Instance;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.producer.FacesContextProducer;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verify that the FacesContextProducer produces the same FacesContext
 * as returned by FacesContext#getCurrentInstance().
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

   @Inject Instance<FacesContext> facesContextInstance;

   @Test
   public void testReturnsCurrentFacesContext()
   {
      new MockFacesContext().set().setCurrentPhaseId(PhaseId.RENDER_RESPONSE);
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
      //Assert.assertEquals(actualFacesContext, producedFacesContext);
      Assert.assertSame(producedFacesContext.getCurrentPhaseId(), PhaseId.RENDER_RESPONSE);
   }

   @Test(expected = ContextNotActiveException.class)
   public void testProducerThrowsExceptionWhenFacesContextNotActive()
   {
      new MockFacesContext().release();
      // NOTE the return value must be invoked to carry out the lookup
      facesContextInstance.get().toString();
   }

   private class MockFacesContext extends FacesContext
   {
      private PhaseId currentPhaseId;

      public FacesContext set()
      {
         setCurrentInstance(this);
         return this;
      }

      @Override
      public void release()
      {
         setCurrentInstance(null);
      }

      @Override
      public PhaseId getCurrentPhaseId()
      {
         return currentPhaseId;
      }

      @Override
      public void setCurrentPhaseId(PhaseId currentPhaseId)
      {
         this.currentPhaseId = currentPhaseId;
      }

      @Override
      public Application getApplication()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public Iterator<String> getClientIdsWithMessages()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public ExternalContext getExternalContext()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public Severity getMaximumSeverity()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public Iterator<FacesMessage> getMessages()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public Iterator<FacesMessage> getMessages(String clientId)
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public RenderKit getRenderKit()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public boolean getRenderResponse()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public boolean getResponseComplete()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public ResponseStream getResponseStream()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public void setResponseStream(ResponseStream stream)
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public ResponseWriter getResponseWriter()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public void setResponseWriter(ResponseWriter writer)
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public UIViewRoot getViewRoot()
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public void setViewRoot(UIViewRoot uivr)
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public void addMessage(String clientId, FacesMessage message)
      {
         throw new UnsupportedOperationException("Not supported");
      }

      @Override
      public void renderResponse()
      {
         throw new UnsupportedOperationException("Not supported.");
      }

      @Override
      public void responseComplete()
      {
         throw new UnsupportedOperationException("Not supported.");
      }
   }
}
