package org.jboss.seam.faces.event;

import java.util.List;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.jboss.logging.Logger;

/**
 * Provide CDI injection to SystemEventListener artifacts by delegating through this class.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
public class DelegatingSystemEventListener extends AbstractListener<SystemEventListener> implements SystemEventListener {
    private transient final Logger log = Logger.getLogger(DelegatingSystemEventListener.class);

    @Override
    public boolean isListenerForSource(final Object source) {
        return true;
    }

    @Override
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        if (event instanceof PreDestroyApplicationEvent && !isBeanManagerAvailable()) {
            log.info("BeanManager no longer available; Cannot notify CDI-managed listeners of "
                    + PreDestroyApplicationEvent.class.getSimpleName());
            return;
        }

        for (SystemEventListener l : getEventListeners()) {
            if (l.isListenerForSource(event.getSource())) {
                l.processEvent(event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<SystemEventListener> getEventListeners() {
        return getListeners(SystemEventBridge.class);
    }

}
