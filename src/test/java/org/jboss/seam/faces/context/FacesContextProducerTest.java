package org.jboss.seam.faces.context;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.jboss.seam.mock.faces.MockFacesContext;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.webbeans.test.AbstractWebBeansTest;
import org.testng.annotations.Test;

/**
 * Verify that the FacesContextProducer produces the same FacesContext
 * as returned by FacesContext#getCurrentInstance().
 *
 * @author Dan Allen
 */
@Artifact(addCurrentPackage = false)
@Classes(FacesContextProducer.class)
public class FacesContextProducerTest extends AbstractWebBeansTest
{
   @Override
   public void beforeMethod()
   {
      super.beforeMethod();
      installFacesContext();
   }

   @Test
   public void testProduceCurrentFacesContext()
   {
      FacesContext actualFacesContext = FacesContext.getCurrentInstance();
      FacesContextProducer standaloneProducer = new FacesContextProducer();
      assertSame(standaloneProducer.getFacesContext(), actualFacesContext);
      
      FacesContext producedFacesContext = getFacesContextInstance();
      // QUESTION how exactly do we verify that we have the correct object if wrapped in proxy?
      assertEquals(producedFacesContext.hashCode(), actualFacesContext.hashCode());
      assertSame(producedFacesContext.getCurrentPhaseId(), PhaseId.RENDER_RESPONSE);
   }

   private void installFacesContext()
   {
      new MockFacesContext().setCurrent().setCurrentPhaseId(PhaseId.RENDER_RESPONSE);
   }

   /**
    * Retrieve the FacesContextProducer instance
    */
   private FacesContext getFacesContextInstance()
   {
      return getCurrentManager().getInstanceByType(FacesContext.class);
   }
}
