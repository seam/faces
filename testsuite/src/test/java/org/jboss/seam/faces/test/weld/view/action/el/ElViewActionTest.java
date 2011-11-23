package org.jboss.seam.faces.test.weld.view.action.el;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;

import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.view.config.ViewConfigDescriptor;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

@RunWith(Arquillian.class)
public class ElViewActionTest {
    private static final String SOLDER_API = "solder-api.jar";
    private static final String SOLDER_IMPL = "solder-impl.jar";
    private static final String SOLDER_LIB_DIR = "target/test-libs/";

    @Deployment
    public static Archive<?> createTestArchive() {
        WebArchive archive = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addAsLibraries(
                        ShrinkWrap.create(ZipImporter.class, SOLDER_API).importFrom(new File(SOLDER_LIB_DIR + SOLDER_API))
                                .as(JavaArchive.class),
                        ShrinkWrap.create(ZipImporter.class, SOLDER_IMPL).importFrom(new File(SOLDER_LIB_DIR + SOLDER_IMPL))
                                .as(JavaArchive.class))
                .addClass(ViewConfigStoreImpl.class).addClass(ElViewActionTest.class)
                .addClass(ElViewActionBean.class).addClass(ElViewActionConfigEnum.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return archive;
    }

    @Inject
    private ViewConfigStore store;

    @Inject
    private ElViewActionBean elViewActionBean;

    private ElViewActionBean mock;

    @Before
    public void setUp() {
        mock = mock(ElViewActionBean.class);
        elViewActionBean.setMock(mock);
    }

    @Test
    public void testViewAction() {
        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/client/done.xhtml");
        callPhaseAndVerifyZeroInteractions(viewConfigDescriptor, PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION);
        viewConfigDescriptor.executeBeforePhase(PhaseId.RENDER_RESPONSE);
        verify(mock).viewAction();
        reset(mock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.RENDER_RESPONSE);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testParameterizedViewAction() {
        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/country/done.xhtml");
        callPhaseAndVerifyZeroInteractions(viewConfigDescriptor, PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION);
        viewConfigDescriptor.executeBeforePhase(PhaseId.RENDER_RESPONSE);
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).parameterizedViewAction("COUNTRIES");
        inOrder.verify(mock).parameterizedViewAction("COUNTRY_CONFIRMED");
        reset(mock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.RENDER_RESPONSE);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testInexistantViewId() {
        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/inexistant.xhtml");
        callPhaseAndVerifyZeroInteractions(viewConfigDescriptor, PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
    }

    @Test
    public void testPhaseViewId() {
        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/explicit-phase/done.xhtml");
        callPhaseAndVerifyZeroInteractions(viewConfigDescriptor, PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.UPDATE_MODEL_VALUES, PhaseId.RENDER_RESPONSE);
        viewConfigDescriptor.executeBeforePhase(PhaseId.INVOKE_APPLICATION);
        verify(mock).viewAction();
        reset(mock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.INVOKE_APPLICATION);
        verifyZeroInteractions(mock);
    }

    private void callPhaseAndVerifyZeroInteractions(ViewConfigDescriptor viewConfigDescriptor, PhaseId... phases) {
        for (PhaseId phase : phases) {
            viewConfigDescriptor.executeBeforePhase(phase);
            viewConfigDescriptor.executeAfterPhase(phase);
            verifyZeroInteractions(mock);
        }
    }
}
