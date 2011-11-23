package org.jboss.seam.faces.test.weld.view.action.binding;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class AController {
    
    private AController mock;
    
    @BeforeRenderResponseViewAction(ViewConfigEnum.Pages.CLIENTS)
    public void beforeRenderResponse() {
        mock.beforeRenderResponse();
    }
    
    @AfterInvokeApplicationViewAction(ViewConfigEnum.Pages.CLIENTS)
    public void afterInvokeApplication() {
        mock.afterInvokeApplication();
    }

    public AController getMock() {
        return mock;
    }

    public void setMock(AController mock) {
        this.mock = mock;
    }
    
}
