package org.jboss.seam.faces.test.weld.view.action.annotation;

import javax.inject.Named;

@Named
public class ClientController {
    public void viewAction() {
    }
    
    @BeforeRenderResponseViewAction(ViewConfigEnum.Pages.CLIENTS)
    public void beforeRenderResponse() {
    }
    
    @AfterInvokeApplicationViewAction(ViewConfigEnum.Pages.CLIENTS)
    public void afterInvokeApplication() {
    }
}
