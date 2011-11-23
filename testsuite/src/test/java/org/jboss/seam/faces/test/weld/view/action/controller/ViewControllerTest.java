package org.jboss.seam.faces.test.weld.view.action.controller;

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

@RunWith(Arquillian.class)
public class ViewControllerTest {
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
    private CountryController countryController;

    private CountryController countryControllerMock;

    @Inject
    private ClientController clientController;

    @Inject
    private DependentBean dependentBean;

    private ClientController clientControllerMock;

    @Before
    public void setUp() {
        countryControllerMock = mock(CountryController.class);
        countryController.setMock(countryControllerMock);
        clientControllerMock = mock(ClientController.class);
        clientController.setMock(clientControllerMock);
    }

    @Test
    public void testClientController() {

        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/client/done.xhtml");
        callPhaseAndVerifyZeroInteractions(viewConfigDescriptor, PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.UPDATE_MODEL_VALUES);
        viewConfigDescriptor.executeBeforePhase(PhaseId.INVOKE_APPLICATION);
        verify(clientControllerMock).beforeInvokeApplication();
        reset(clientControllerMock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.INVOKE_APPLICATION);
        verify(clientControllerMock).afterInvokeApplication();
        verify(clientControllerMock).afterInvokeApplication2();
        reset(clientControllerMock);
        viewConfigDescriptor.executeBeforePhase(PhaseId.RENDER_RESPONSE);
        verify(clientControllerMock).beforeRenderResponse();
        reset(clientControllerMock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.RENDER_RESPONSE);
        verifyZeroInteractions(clientControllerMock);
        verifyZeroInteractions(countryControllerMock);
    }

    @Test
    public void testCountryController() {

        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/country/done.xhtml");
        callPhaseAndVerifyZeroInteractions(viewConfigDescriptor, PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION);
        viewConfigDescriptor.executeBeforePhase(PhaseId.RENDER_RESPONSE);
        verify(countryControllerMock).beforeRenderResponse();
        reset(countryControllerMock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.RENDER_RESPONSE);
        verify(countryControllerMock).afterRenderResponse(dependentBean);
        verifyZeroInteractions(clientControllerMock);
    }

    @Test
    public void testMultipleControllers() {

        ViewConfigDescriptor viewConfigDescriptor = store.getRuntimeViewConfigDescriptor("/multiple/done.xhtml");
        callPhaseAndVerifyZeroInteractions(viewConfigDescriptor, PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.UPDATE_MODEL_VALUES);
        viewConfigDescriptor.executeBeforePhase(PhaseId.INVOKE_APPLICATION);
        verify(clientControllerMock).beforeInvokeApplication();
        reset(clientControllerMock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.INVOKE_APPLICATION);
        verify(clientControllerMock).afterInvokeApplication();
        verify(clientControllerMock).afterInvokeApplication2();
        reset(clientControllerMock);
        verifyZeroInteractions(countryControllerMock);
        viewConfigDescriptor.executeBeforePhase(PhaseId.RENDER_RESPONSE);
        verify(countryControllerMock).beforeRenderResponse();
        verify(clientControllerMock).beforeRenderResponse();
        reset(clientControllerMock, countryControllerMock);
        viewConfigDescriptor.executeAfterPhase(PhaseId.RENDER_RESPONSE);
        verify(countryControllerMock).afterRenderResponse(dependentBean);
        verifyZeroInteractions(clientControllerMock);
    }

    private void callPhaseAndVerifyZeroInteractions(ViewConfigDescriptor viewConfigDescriptor, PhaseId... phases) {
        for (PhaseId phase : phases) {
            viewConfigDescriptor.executeBeforePhase(phase);
            viewConfigDescriptor.executeAfterPhase(phase);
            verifyZeroInteractions(countryControllerMock, clientControllerMock);
        }
    }
}
