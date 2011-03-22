package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.seam.solder.core.Requires;
import org.jboss.seam.solder.el.Expressions;

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
   private Expressions expressions;
   @Inject
   private Event<SecurityCheckEvent> securityCheckEvent;

   public void enforce(@Observes PreRenderViewEvent event) {
        log.info("PostConstructViewMapEvent");
        UIViewRoot viewroot = (UIViewRoot) event.getSource();
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
