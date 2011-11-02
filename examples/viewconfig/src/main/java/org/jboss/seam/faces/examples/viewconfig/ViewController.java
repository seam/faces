package org.jboss.seam.faces.examples.viewconfig;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;

public class ViewController {

    @Before
    @ApplyRequestValues
    public void beforeApplyRequestValues() {
        addFacesMessage(this.getClass().getSimpleName() + ".beforeApplyRequestValues was called");
    }

    @After
    @ApplyRequestValues
    public void afterApplyRequestValues() {
        addFacesMessage(this.getClass().getSimpleName() + ".afterApplyRequestValues was called");
    }

    @Before
    @ProcessValidations
    public void beforeProcessValidations() {
        addFacesMessage(this.getClass().getSimpleName() + ".beforeProcessValidations was called");
    }

    @After
    @ProcessValidations
    public void afterProcessValidations() {
        addFacesMessage(this.getClass().getSimpleName() + ".afterProcessValidations was called");
    }

    @Before
    @UpdateModelValues
    public void beforeUpdateModelValues() {
        addFacesMessage(this.getClass().getSimpleName() + ".beforeUpdateModelValues was called");
    }

    @After
    @UpdateModelValues
    public void afterUpdateModelValues() {
        addFacesMessage(this.getClass().getSimpleName() + ".afterUpdateModelValues was called");
    }

    @Before
    @InvokeApplication
    public void beforeInvokeApplication() {
        addFacesMessage(this.getClass().getSimpleName() + ".beforeInvokeApplication was called");
    }

    @After
    @InvokeApplication
    public void afterInvokeApplication() {
        addFacesMessage(this.getClass().getSimpleName() + ".afterInvokeApplication was called");
    }

    @Before
    @RenderResponse
    public void beforeRenderResponse() {
        addFacesMessage(this.getClass().getSimpleName() + ".beforeRenderResponse was called");
    }

    @After
    @RenderResponse
    public void afterRenderResponse() {
        addFacesMessage(this.getClass().getSimpleName() + ".RenderResponse was called");
    }

    private void addFacesMessage(String message) {
        FacesMessage facesMessages = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessages);
    }
}
