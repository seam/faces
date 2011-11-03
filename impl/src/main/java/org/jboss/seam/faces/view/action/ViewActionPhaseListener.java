package org.jboss.seam.faces.view.action;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.solder.logging.Logger;

/**
 * Use the annotations stored in the ViewConfigStore to execute action (MethodExpression) calls.
 * 
 * Class similar to <code>SecurityPhaseListener</code>.
 * 
 * @author Adriàn Gonzalez
 */
public class ViewActionPhaseListener {

    private transient final Logger log = Logger.getLogger(ViewActionPhaseListener.class);

    @Inject
    private ViewControllerStore viewControllerStore;

    // TODO : should be executed after SecurityPhaseListener
    public void observerBeforePhase(@Observes @Before PhaseEvent event) {
        PhaseId phaseId = event.getPhaseId();
        log.debugf("Before {1} event", phaseId);
        if (event.getFacesContext().getViewRoot() == null) {
            log.debug("viewRoot null, skipping view actions");
            return;
        }
        List<ViewControllerDescriptor> viewControllers = viewControllerStore.getControllerDescriptors(event.getFacesContext()
                .getViewRoot().getViewId());
        for (int i = viewControllers.size(); --i >= 0;) {
            ViewControllerDescriptor viewControllerDescriptor = viewControllers.get(i);
            viewControllerDescriptor.executeBeforePhase(event.getPhaseId());
        }
    }

    public void observerAfterPhase(@Observes @After PhaseEvent event) {
        PhaseId phaseId = event.getPhaseId();
        log.debugf("After {1} event", phaseId);
        List<ViewControllerDescriptor> viewControllers = viewControllerStore.getControllerDescriptors(event.getFacesContext()
                .getViewRoot().getViewId());
        for (ViewControllerDescriptor viewControllerDescriptor : viewControllers) {
            viewControllerDescriptor.executeAfterPhase(event.getPhaseId());
        }
    }
}
