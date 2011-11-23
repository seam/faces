package org.jboss.seam.faces.test.weld.view.action.el;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class ElViewActionBean {
    private ElViewActionBean mock;
    
    public void viewAction() {
        mock.viewAction();
    }
    
    public void parameterizedViewAction(String message) {
        mock.parameterizedViewAction(message);
    }

    public ElViewActionBean getMock() {
        return mock;
    }

    public void setMock(ElViewActionBean mock) {
        this.mock = mock;
    }
}
