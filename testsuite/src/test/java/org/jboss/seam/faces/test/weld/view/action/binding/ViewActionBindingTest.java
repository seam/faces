package org.jboss.seam.faces.test.weld.view.action.binding;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.view.config.ViewConfigDescriptor;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

@RunWith(Arquillian.class)
public class ViewActionBindingTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addClass(ViewConfigStoreImpl.class)
                .addPackage(ViewConfigEnum.class.getPackage())
                .addAsManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
        return archive;
    }

    @Inject
    private ViewConfigStore store;

    @Inject
    private AController aController;

    private AController aControllerMock;

    @Inject
    private OrderedController orderedController;

    private OrderedController orderedControllerMock;

    @Before
    public void setUp() {
        aControllerMock = mock(AController.class);
        aController.setMock(aControllerMock);
        orderedControllerMock = mock(OrderedController.class);
        orderedController.setMock(orderedControllerMock);
    }

    @Test
    public void testAController() {

        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/client/done.xhtml");
        callPhaseAndVerifyZeroInteractions(viewConfigDescriptor, PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.UPDATE_MODEL_VALUES);
        viewConfigDescriptor.executeBeforePhase(PhaseId.INVOKE_APPLICATION);
        verifyZeroInteractions(aControllerMock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.INVOKE_APPLICATION);
        verify(aControllerMock).afterInvokeApplication();
        reset(aControllerMock);
        viewConfigDescriptor.executeBeforePhase(PhaseId.RENDER_RESPONSE);
        verify(aControllerMock).beforeRenderResponse();
        reset(aControllerMock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.RENDER_RESPONSE);
        verifyZeroInteractions(aControllerMock);
    }

    @Test
    public void testOrder() {

        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/order.xhtml");
        viewConfigDescriptor.executeAfterPhase(PhaseId.INVOKE_APPLICATION);
        InOrder inOrder = inOrder(orderedControllerMock);
        inOrder.verify(orderedControllerMock).order1();
        inOrder.verify(orderedControllerMock).lowOrder();
        inOrder.verify(orderedControllerMock).middleOrder();
        inOrder.verify(orderedControllerMock).order600();
        inOrder.verify(orderedControllerMock).highOrder();
    }

    private void callPhaseAndVerifyZeroInteractions(ViewConfigDescriptor viewConfigDescriptor, PhaseId... phases) {
        for (PhaseId phase : phases) {
            viewConfigDescriptor.executeBeforePhase(phase);
            viewConfigDescriptor.executeAfterPhase(phase);
            verifyZeroInteractions(aControllerMock);
        }
    }
}
