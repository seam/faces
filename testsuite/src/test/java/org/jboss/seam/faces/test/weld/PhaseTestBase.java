/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.faces.test.weld;

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
