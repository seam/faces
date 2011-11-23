package org.jboss.seam.faces.test.weld.view.action.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.RenderResponse;

@Named
@ApplicationScoped
public class ClientController {

    private ClientController mock;

    @Before
    @RenderResponse
    public void beforeRenderResponse() {
        mock.beforeRenderResponse();
    }

    @Before
    @InvokeApplication
    public void beforeInvokeApplication() {
        mock.beforeInvokeApplication();
    }

    @After
    @InvokeApplication
    public void afterInvokeApplication() {
        mock.afterInvokeApplication();
    }

    @After
    @InvokeApplication
    public void afterInvokeApplication2() {
        mock.afterInvokeApplication2();
    }

    public ClientController getMock() {
        return mock;
    }

    public void setMock(ClientController mock) {
        this.mock = mock;
    }

}
