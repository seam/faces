package org.jboss.seam.faces.test.weld.view.action;

import java.util.List;
import java.util.Map;

import javax.faces.event.PhaseId;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.test.weld.view.action.annotation.AfterInvokeApplicationViewAction;
import org.jboss.seam.faces.test.weld.view.action.annotation.BeforeRenderResponseViewAction;
import org.jboss.seam.faces.test.weld.view.action.annotation.ClientController;
import org.jboss.seam.faces.test.weld.view.action.annotation.CountryController;
import org.jboss.seam.faces.test.weld.view.action.annotation.ViewConfigEnum;
import org.jboss.seam.faces.view.action.PhaseInstant;
import org.jboss.seam.faces.view.action.ViewActionStrategy;
import org.jboss.seam.faces.view.action.ViewControllerDescriptor;
import org.jboss.seam.faces.view.action.ViewControllerExtension;
import org.jboss.seam.faces.view.action.ViewControllerStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ViewControllerDescriptorTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addClass(ViewConfigStoreImpl.class)
                .addClass(ViewControllerStore.class).addClass(ViewControllerExtension.class)
                .addClass(AfterInvokeApplicationViewAction.class).addClass(BeforeRenderResponseViewAction.class)
                .addClass(ClientController.class).addClass(CountryController.class).addClass(ViewConfigEnum.class)
                .addPackage(ViewConfigEnum.class.getPackage())
                .addAsManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
        return archive;
    }

    @Inject
    private ViewControllerStore store;

    @Test
    public void testGetControllerDescriptors() {

        List<ViewControllerDescriptor> descriptors = store.getControllerDescriptors("/client/done.xhtml");
        Assert.assertEquals(2, descriptors.size());
        ViewControllerDescriptor descriptor = descriptors.get(0);
        Assert.assertEquals("/client/*", descriptor.getViewId());
        Map<PhaseInstant, List<ViewActionStrategy>> phaseMethods = descriptor.getPhaseMethods();
        List<ViewActionStrategy> viewActions = phaseMethods.get(PhaseInstant.BEFORE_RENDER_RESPONSE);
        Assert.assertEquals(1, viewActions.size());
        ViewActionStrategy actionStrategy = viewActions.get(0);
        Assert.assertEquals("#{clientController.viewAction}",
                ((ViewControllerDescriptor.MethodExpressionInvoker) actionStrategy).getMethodExpressionString());
        descriptor = descriptors.get(1);
        Assert.assertEquals("/client/*", descriptor.getViewId());
        phaseMethods = descriptor.getPhaseMethods();
        Assert.assertEquals(2, phaseMethods.size());
        Assert.assertEquals(1, phaseMethods.get(PhaseInstant.BEFORE_RENDER_RESPONSE).size());
        Assert.assertEquals(1, phaseMethods.get(new PhaseInstant(PhaseId.INVOKE_APPLICATION, false)).size());

        descriptors = store.getControllerDescriptors("/country/done.xhtml");
        Assert.assertEquals(1, descriptors.size());
        descriptor = descriptors.get(0);
        Assert.assertEquals("/country/*", descriptor.getViewId());
        Assert.assertEquals(CountryController.class, descriptor.getViewControllerClass());
        phaseMethods = descriptor.getPhaseMethods();
        Assert.assertEquals(1, phaseMethods.size());
        Assert.assertEquals(1, phaseMethods.get(PhaseInstant.BEFORE_RENDER_RESPONSE).size());
    }
}
