package org.jboss.seam.faces.test.view.config;

import java.lang.annotation.Annotation;
import java.util.List;
import org.jboss.seam.faces.test.view.config.annotation.QualifiedIconLiteral;
import org.jboss.seam.faces.test.view.config.annotation.IconLiteral;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.test.view.config.annotation.RestrictedLiteral;
import org.jboss.seam.faces.test.view.config.annotation.ViewConfigEnum;
import org.jboss.seam.faces.view.config.RestrictAtPhaseDefault;
import org.jboss.seam.faces.view.config.SecurityPhaseListener;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.seam.security.events.AuthorizationCheckEvent;
import org.jboss.seam.security.extension.SecurityExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@RunWith(Arquillian.class)
public class SecurityPhaseListenerTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addClass(ViewConfigStoreImpl.class)
                .addClass(SecurityPhaseListener.class)
                .addClass(AuthorizationCheckEvent.class)
                .addClass(SecurityBindingType.class)
                .addClass(SecurityExtension.class)
                .addClass(PhaseIdType.class)
                .addClass(RestrictAtPhaseDefault.class)
                .addPackage(RenderResponse.class.getPackage())
                .addPackage(ViewConfigEnum.class.getPackage())
                .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
        return archive;
    }

    @Inject
    private ViewConfigStore store;

    @Inject
    private SecurityPhaseListener listener;

    @Before
    public void setup() {
        store = new ViewConfigStoreImpl();
        store.addAnnotationData("/*", new IconLiteral("default.gif"));
        store.addAnnotationData("/sad/*", new IconLiteral("sad.gif"));
        store.addAnnotationData("/happy/*", new IconLiteral("happy.gif"));
        store.addAnnotationData("/happy/done.xhtml", new IconLiteral("finished.gif"));
        store.addAnnotationData("/qualified/yes.xhtml", new RestrictedLiteral());
        store.addAnnotationData("/qualified/yes.xhtml", new QualifiedIconLiteral("qualified.gif"));
        System.out.println("** Finished adding annotation data *****************");
    }

    @Test
    @Ignore
    public void testIsAnnotationApplicableToPhase() {
        Assert.assertEquals(true, listener.isAnnotationApplicableToPhase(new RestrictedLiteral(), PhaseIdType.RENDER_RESPONSE));
        Assert.assertEquals(false, listener.isAnnotationApplicableToPhase(new RestrictedLiteral(PhaseIdType.RESTORE_VIEW), PhaseIdType.RENDER_RESPONSE));
        Assert.assertEquals(true, listener.isAnnotationApplicableToPhase(new RestrictedLiteral(PhaseIdType.RESTORE_VIEW), PhaseIdType.RESTORE_VIEW));
    }
            
    
    @Test
    public void testIsRestrictPhase() {
        List<? extends Annotation> restrict;
        restrict = listener.getRestrictionsForPhase(PhaseIdType.RENDER_RESPONSE, "/qualified/yes.xhtml");
        Assert.assertEquals(1, restrict.size());

        restrict = listener.getRestrictionsForPhase(PhaseIdType.RESTORE_VIEW, "/qualified/yes.xhtml");
        Assert.assertEquals(null, restrict);

        restrict = listener.getRestrictionsForPhase(PhaseIdType.RENDER_RESPONSE, "/happy/cat.xhtml");
        Assert.assertEquals(null, restrict);

    }
}
