package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.seam.solder.core.Requires;

/**
 * Use the annotations stored in the ViewConfigStore to restrict view access
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Requires("org.jboss.seam.security.extension.SecurityExtension")
public class ViewConfigSecurityEnforcer
{
   private transient final Logger log = Logger.getLogger(ViewConfigSecurityEnforcer.class);

   @Inject
   private ViewConfigStore viewConfigStore;
   @Inject
   private Event<SecurityCheckEvent> securityCheckEvent;

   public void enforce(@Observes @Before @RenderResponse PhaseEvent event) {
        log.info("Before Render Response event");
        
        UIViewRoot viewroot = (UIViewRoot) event.getFacesContext().getViewRoot();
        List<? extends Annotation> annotations = viewConfigStore.getAllQualifierData(viewroot.getViewId(), SecurityBindingType.class);
        if (annotations == null || annotations.isEmpty()) {
            log.info("Annotations is null/empty");
            return;
        }
        SecurityCheckEvent securityEvent = new SecurityCheckEvent(annotations);
        securityCheckEvent.fire(securityEvent);
        if (! securityEvent.isAuthorized()) {
            throw new AbortProcessingException("Access denied");
        }
        log.info("Access allowed");
    }
}
