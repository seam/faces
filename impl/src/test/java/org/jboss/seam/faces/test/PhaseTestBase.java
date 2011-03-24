package org.jboss.seam.faces.test;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.seam.faces.event.PhaseEventBridge;
import org.jboss.test.faces.mock.lifecycle.MockLifecycle;
import org.jboss.test.faces.stub.faces.StubFacesContext;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PhaseTestBase {
    @Inject
    PhaseEventBridge phaseEventBridge;

    protected FacesContext facesContext = new StubFacesContext();
    protected final MockLifecycle lifecycle = new MockLifecycle();

    static List<PhaseId> ALL_PHASES = new ArrayList<PhaseId>() {
        private static final long serialVersionUID = 1L;

        {
            add(PhaseId.APPLY_REQUEST_VALUES);
            add(PhaseId.INVOKE_APPLICATION);
            add(PhaseId.PROCESS_VALIDATIONS);
            add(PhaseId.RENDER_RESPONSE);
            add(PhaseId.RESTORE_VIEW);
            add(PhaseId.UPDATE_MODEL_VALUES);
        }
    };

    protected void fireAllPhases() {
        fireAllBeforePhases();
        fireAllAfterPhases();
    }

    protected void fireAllBeforePhases() {
        fireBeforePhases(ALL_PHASES);
    }

    protected void fireBeforePhases(final List<PhaseId> phases) {
        for (PhaseId phaseId : phases) {
            fireBeforePhase(phaseId);
        }
    }

    protected void fireBeforePhase(final PhaseId phaseId) {
        phaseEventBridge.beforePhase(new PhaseEvent(facesContext, phaseId, lifecycle));
    }

    protected void fireAllAfterPhases() {
        fireAfterPhases(ALL_PHASES);
    }

    protected void fireAfterPhases(final List<PhaseId> phases) {
        for (PhaseId phaseId : phases) {
            fireAfterPhase(phaseId);
        }
    }

    protected void fireAfterPhase(final PhaseId phaseId) {
        phaseEventBridge.afterPhase(new PhaseEvent(facesContext, phaseId, lifecycle));
    }
}
