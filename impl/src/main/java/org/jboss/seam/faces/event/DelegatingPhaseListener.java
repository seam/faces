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
package org.jboss.seam.faces.event;

import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.jboss.seam.faces.context.RenderScopedContext;
import org.jboss.seam.faces.transaction.TransactionPhaseListener;

/**
 * Provide CDI injection to PhaseListener artifacts by delegating through this class.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
public class DelegatingPhaseListener extends AbstractListener<PhaseListener> implements PhaseListener {
    private static final long serialVersionUID = 8454616175394888259L;

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    public void beforePhase(final PhaseEvent event) {
        for (PhaseListener listener : getPhaseListeners()) {
            if (shouldProcessPhase(listener, event)) {
                listener.beforePhase(event);
            }
        }
    }

    public void afterPhase(final PhaseEvent event) {
        for (PhaseListener listener : getPhaseListeners()) {
            if (shouldProcessPhase(listener, event)) {
                listener.afterPhase(event);
            }
        }
    }

    /**
     * Determine if the {@link PhaseListener} should process the given {@link PhaseEvent}.
     */
    private boolean shouldProcessPhase(final PhaseListener listener, final PhaseEvent event) {
        return (PhaseId.ANY_PHASE.equals(listener.getPhaseId()) || event.getPhaseId().equals(listener.getPhaseId()));
    }

    @SuppressWarnings("unchecked")
    private List<PhaseListener> getPhaseListeners() {
        return getEnabledListeners(RenderScopedContext.class, PhaseEventBridge.class, TransactionPhaseListener.class);
    }

}
