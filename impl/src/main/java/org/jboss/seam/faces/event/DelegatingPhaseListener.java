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
