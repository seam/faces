package org.jboss.seam.faces.international;

import static org.testng.Assert.assertSame;

import java.util.Locale;

import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseId;

import org.jboss.seam.el.SeamEL;
import org.jboss.seam.faces.context.FacesContextProducer;
import org.jboss.seam.international.LocaleProducer;
import org.jboss.seam.international.LocaleResolver;
import org.jboss.seam.international.LocaleResolverProducer;
import org.jboss.seam.mock.faces.MockFacesContext;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.webbeans.test.AbstractWebBeansTest;
import org.testng.annotations.Test;

@Artifact(addCurrentPackage = false)
@Classes({LocaleProducer.class, LocaleResolver.class, LocaleResolverProducer.class, FacesLocaleResolver.class, FacesContextProducer.class})
public class FacesLocaleProducerTest extends AbstractWebBeansTest
{
   @Override
   public void beforeMethod()
   {
      installMockFacesContext();
   }
   
   // FIXME broken
   //@Test
   public void testGetLocaleFromViewRoot()
   {
      assertSame(getLocaleInstance(), Locale.FRANCE);
   }
   
   public Locale getLocaleInstance()
   {
      return getCurrentManager().getInstanceByType(Locale.class);
   }
   
   private void installMockFacesContext()
   {
      MockFacesContext facesContext = new MockFacesContext();
      facesContext.setCurrentPhaseId(PhaseId.RENDER_RESPONSE);
      facesContext.setCurrent();
      // must have ELContext set to set the locale on the UIViewRoot
      facesContext.setELContext(SeamEL.createELContext());
      UIViewRoot viewRoot = new UIViewRoot();
      viewRoot.setLocale(Locale.FRANCE);
      facesContext.setViewRoot(viewRoot);
   }
}
