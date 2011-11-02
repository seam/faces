package org.jboss.seam.faces.examples.viewconfig;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jboss.seam.faces.examples.viewconfig.MyAppViewConfig.Pages;

@Named
@RequestScoped
public class ViewActionBindingTypeController {
    @MyViewAction(Pages.VIEW_ACTION_BINDING_TYPE)
    public void beforeRenderAction() {
        FacesMessage facesMessages = new FacesMessage("ViewActionBindingTypeController.beforeRenderAction was called");
        FacesContext.getCurrentInstance().addMessage(null, facesMessages);
    }
}
