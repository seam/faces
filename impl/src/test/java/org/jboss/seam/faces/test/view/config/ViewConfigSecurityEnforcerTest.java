package org.jboss.seam.faces.test.view.config;

import org.jboss.seam.faces.test.view.config.annotation.QualifiedUrlLiteral;
import org.jboss.seam.faces.test.view.config.annotation.QualifiedIconLiteral;
import org.jboss.seam.faces.test.view.config.annotation.IconLiteral;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.test.view.config.annotation.ViewConfigEnum;
import org.jboss.seam.faces.view.config.ViewConfigSecurityEnforcer;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.seam.security.extension.SecurityExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author bleathem
 */
@RunWith(Arquillian.class)
public class ViewConfigSecurityEnforcerTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            .addClass(ViewConfigStoreImpl.class)
            .addClass(ViewConfigSecurityEnforcer.class)
            .addClass(SecurityBindingType.class)
            .addClass(SecurityExtension.class)
            .addClass(PhaseIdType.class)
            .addPackage(RenderResponse.class.getPackage())
            .addPackage(ViewConfigEnum.class.getPackage())
            .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
      return archive;
   }

   @Inject
   ViewConfigStore store;
    
   @Inject
   ViewConfigSecurityEnforcer enforcer;

   
//   @Before
   public void setup()
   {
      store  = new ViewConfigStoreImpl();
      store.addAnnotationData("/*", new IconLiteral("default.gif"));
      store.addAnnotationData("/sad/*", new IconLiteral("sad.gif"));
      store.addAnnotationData("/happy/*", new IconLiteral("happy.gif"));
      store.addAnnotationData("/happy/done.xhtml", new IconLiteral("finished.gif"));
      store.addAnnotationData("/qualified/yes.xhtml", new QualifiedUrlLiteral("http://example.com"));
      store.addAnnotationData("/qualified/yes.xhtml", new QualifiedIconLiteral("qualified.gif"));
   }
   
   @Test
   public void testIsRestrictPhase()
   {
      setup();
      boolean restrict;
      restrict = enforcer.isRestrictPhase(PhaseIdType.RENDER_RESPONSE, "/happy/cat.xhtml",  true);
      Assert.assertEquals(false, restrict);
      
      restrict = enforcer.isRestrictPhase(PhaseIdType.RENDER_RESPONSE, "/happy/cat.xhtml",  false);
      Assert.assertEquals(true, restrict);
      
      restrict = enforcer.isRestrictPhase(PhaseIdType.INVOKE_APPLICATION, "/happy/cat.xhtml",  true);
      Assert.assertEquals(true, restrict);
      
      restrict = enforcer.isRestrictPhase(PhaseIdType.INVOKE_APPLICATION, "/happy/cat.xhtml",  false);
      Assert.assertEquals(false, restrict);
      
      restrict = enforcer.isRestrictPhase(PhaseIdType.RESTORE_VIEW, "/happy/cat.xhtml",  true);
      Assert.assertEquals(false, restrict);
      
      restrict = enforcer.isRestrictPhase(PhaseIdType.RESTORE_VIEW, "/happy/cat.xhtml",  false);
      Assert.assertEquals(false, restrict);
   }
}










