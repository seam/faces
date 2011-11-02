package org.jboss.seam.faces.examples.viewconfig;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class ViewActionController {
    public void preRenderAction() {
        FacesMessage facesMessages = new FacesMessage("ViewActionController.preRenderAction was called");
        FacesContext.getCurrentInstance().addMessage(null, facesMessages);
    }
}
