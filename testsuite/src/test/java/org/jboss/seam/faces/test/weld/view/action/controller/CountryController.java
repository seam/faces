package org.jboss.seam.faces.test.weld.view.action.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.RenderResponse;

@Named
@ApplicationScoped
public class CountryController {
    
    private CountryController mock;

    @Before
    @RenderResponse
    public void beforeRenderResponse() {
        mock.beforeRenderResponse();
    }
    
    @After
    @RenderResponse
    public void afterRenderResponse(DependentBean dependenBean) {
        mock.afterRenderResponse(dependenBean);
    }

    public CountryController getMock() {
        return mock;
    }

    public void setMock(CountryController mock) {
        this.mock = mock;
    }
}
