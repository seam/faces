package org.jboss.seam.faces.test.weld.view.action.annotation;

import javax.inject.Named;

import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.RenderResponse;

@Named
public class CountryController {
    
    @Before
    @RenderResponse
    public void beforeRenderResponse() {
    }
}
