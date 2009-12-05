package org.jboss.seam.faces.el;

import static org.testng.Assert.assertSame;

import javax.el.CompositeELResolver;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.jboss.seam.el.Expressions;
import org.jboss.seam.el.ExpressionsProducer;
import org.jboss.seam.el.SeamEL;
import org.jboss.seam.faces.context.FacesContextProducer;
import org.jboss.seam.mock.faces.MockFacesContext;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.webbeans.test.AbstractWebBeansTest;
import org.testng.annotations.Test;

/**
 * First and foremost, ensure that FacesExpressions is configured properly
 * to specialize Expressions and will thus load successfully. Once loaded,
 * verify that Expressions adds the appropriate JSF-specific functionality.
 *
 * @author Dan Allen
 */
@Artifact(addCurrentPackage = false)
@Classes(
{
   FacesExpressions.class, Expressions.class, ExpressionsProducer.class, FacesContextProducer.class
})
public class FacesExpressionsTest extends AbstractWebBeansTest
{
   @Override
   public void beforeMethod()
   {
      super.beforeMethod();
      installMockFacesContext();
   }

   // FIXME broken
   //@Test
   public void testUsesELFromFacesContext()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      facesContext.setCurrentPhaseId(PhaseId.RENDER_RESPONSE);
      Expressions expressions = getExpressionInstance();
      assertSame(expressions.getELContext(), facesContext.getELContext());
   }

   private Expressions getExpressionInstance()
   {
      // we can't check that we have a FacesExpressions because it is hidden behind a proxy object
      return getCurrentManager().getInstanceByType(Expressions.class);
   }

   private void installMockFacesContext()
   {
      MockFacesContext facesContext = new MockFacesContext();
      facesContext.setELContext(SeamEL.createELContext(new CompositeELResolver()));
      facesContext.setCurrent();
   }
}
