package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Observes;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.security.Restrict;
import org.jboss.seam.security.annotations.Secures;
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
   private static final Logger log = Logger.getLogger(ViewConfigSecurityEnforcer.class);

   @Inject
   private ViewConfigStore viewConfigStore;
   @Inject
   private Expressions expressions;

   public void enforce(@Observes PreRenderViewEvent event) {
        log.info("PostConstructViewMapEvent");
        UIViewRoot viewroot = (UIViewRoot) event.getSource();
        List<? extends Annotation> annotations = viewConfigStore.getAllQualifierData(viewroot.getViewId(), SecurityBindingType.class);
        if (annotations == null || annotations.isEmpty()) {
            log.info("Annotations is null/empty");
            return;
        }
        for (Annotation annotationLoop : annotations) {
            Restrict annotation = (Restrict) annotationLoop;
            log.info("Evaluating Annotation");
            String el = annotation.value();
            Boolean allowed = expressions.evaluateMethodExpression(el, Boolean.class);
            if (allowed) {
                log.info("Access allowed");
                return;
            } else {
                log.info("Access denied");
                throw new AbortProcessingException("Access denied");
            }
        }
    }
   
   private <T extends Annotation> List<T> getSecurityBindingTypes(List<T> allAnnotations)
   {
       if (allAnnotations == null || allAnnotations.isEmpty())
       {
           return null;
       }
       List<T> securityAnnotations = new ArrayList<T>();
       for (T annotation : allAnnotations) {
           if (annotation.getClass().isAnnotationPresent(SecurityBindingType.class)) {
               securityAnnotations.add(annotation);
           }
       }
       return securityAnnotations;
   }
   
}
