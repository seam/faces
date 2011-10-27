package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.inject.Inject;

import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.reflection.AnnotationInspector;

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
    private ViewConfigStore viewConfigStore;
    @Inject
    private BeanManager beanManager;

    /**
     * Execute any action annotations applicable to the RestoreView phase
     *
     * @param event
     */
    public void observeRestoreView(@Observes @After @RestoreView PhaseEvent event) {
        log.debug("After Restore View event");
        performObservation(event, PhaseIdType.RESTORE_VIEW);
    }

    /**
     * Execute any action annotations applicable to the ApplyRequestValues phase
     *
     * @param event
     */
    public void observeApplyRequestValues(@Observes @Before @ApplyRequestValues PhaseEvent event) {
        log.debug("After Apply Request Values event");
        performObservation(event, PhaseIdType.APPLY_REQUEST_VALUES);
    }

    /**
     * Execute any action annotations applicable to the ProcessValidations phase
     *
     * @param event
     */
    public void observeProcessValidations(@Observes @Before @ProcessValidations PhaseEvent event) {
        log.debug("After Process Validations event");
        performObservation(event, PhaseIdType.PROCESS_VALIDATIONS);
    }

    /**
     * Execute any action annotations applicable to the UpdateModelValues phase
     *
     * @param event
     */
    public void observeUpdateModelValues(@Observes @Before @UpdateModelValues PhaseEvent event) {
        log.debug("After Update Model Values event");
        performObservation(event, PhaseIdType.UPDATE_MODEL_VALUES);
    }

    /**
     * Execute any action annotations applicable to the InvokeApplication phase
     *
     * @param event
     */
    public void observeInvokeApplication(@Observes @Before @InvokeApplication PhaseEvent event) {
        log.debug("Before Render Response event");
        performObservation(event, PhaseIdType.INVOKE_APPLICATION);
    }

    /**
     * Execute any action annotations applicable to the RenderResponse phase
     *
     * @param event
     */
    public void observeRenderResponse(@Observes @Before @RenderResponse PhaseEvent event) {
        log.debug("Before Render Response event");
        performObservation(event, PhaseIdType.RENDER_RESPONSE);
    }

    /**
     * Inspect the annotations in the ViewConfigStore, executing any actions applicable to this phase
     *
     * @param event
     * @param phaseIdType
     */
    private void performObservation(PhaseEvent event, PhaseIdType phaseIdType) {
        UIViewRoot viewRoot = (UIViewRoot) event.getFacesContext().getViewRoot();
        List<? extends Annotation> actionsForPhase = getViewActionsForPhase(phaseIdType, viewRoot.getViewId());
        if (actionsForPhase != null) {
            log.debugf("Enforcing on phase %s", phaseIdType);
            execute(event.getFacesContext(), viewRoot, actionsForPhase);
        }
    }

    /**
     * Retrieve all annotations from the ViewConfigStore for a given a JSF phase, and a view id,
     * and where the annotation is qualified by @SecurityBindingType
     *
     * @param currentPhase
     * @param viewId
     * @return list of restrictions applicable to this viewId and PhaseTypeId
     */
    public List<? extends Annotation> getViewActionsForPhase(PhaseIdType currentPhase, String viewId) {
        List<? extends Annotation> allViewActionAnnotations = viewConfigStore.getAllQualifierData(viewId, ViewActionBindingType.class);
        List<Annotation> applicableViewActionAnnotations = null;
        for (Annotation annotation : allViewActionAnnotations) {
            PhaseIdType defaultPhase = getDefaultPhase(viewId);
            if (isAnnotationApplicableToPhase(annotation, currentPhase, defaultPhase)) {
                if (applicableViewActionAnnotations == null) { // avoid spawning arrays at all phases of the lifecycle
                    applicableViewActionAnnotations = new ArrayList<Annotation>();
                }
                applicableViewActionAnnotations.add(annotation);
            }
        }
        return applicableViewActionAnnotations;
    }

    /**
     * Inspect an annotation to see if it specifies a view in which it should be.  Fall back on default view otherwise.
     *
     * @param annotation
     * @param currentPhase
     * @param defaultPhases
     * @return true if the annotation is applicable to this view and phase, false otherwise
     */
    public boolean isAnnotationApplicableToPhase(Annotation annotation, PhaseIdType currentPhase, PhaseIdType defaultPhase) {
        Method phaseAtViewActionMethod = getPhaseAtViewActionMethod(annotation);
        PhaseIdType phasedId = null;
        if (phaseAtViewActionMethod != null) {
            log.debug("Annotation %s is using the phase method.");
            phasedId = getViewActionPhaseId(phaseAtViewActionMethod, annotation);
        }
        Phase phaseQualifier = AnnotationInspector.getAnnotation(annotation.annotationType(), Phase.class, beanManager);
        if (phaseQualifier != null) {
            log.debug("Using Phase found in @Phase qualifier on the annotation.");
            phasedId = phaseQualifier.value();
        }
        if (phasedId == null) {
            log.debug("Falling back on default phase id");
            phasedId = defaultPhase;
        }
        return phasedId == currentPhase;
    }

    /**
     * Get the default phases at which restrictions should be applied, by looking for a @Phase default value
     *
     * @param viewId
     * @return default phase
     */
    public PhaseIdType getDefaultPhase(String viewId) {
        return PhaseDefault.DEFAULT_PHASE;
    }

    /**
     * Utility method to extract the "phase" method from an annotation
     *
     * @param annotation
     * @return phaseAtViewActionMethod if found, null otherwise
     */
    public Method getPhaseAtViewActionMethod(Annotation annotation) {
        Method phaseAtViewActionMethod;
        try {
        	phaseAtViewActionMethod = annotation.annotationType().getDeclaredMethod("phase");
        } catch (NoSuchMethodException ex) {
        	phaseAtViewActionMethod = null;
        } catch (SecurityException ex) {
            throw new IllegalArgumentException("phase method must be accessible", ex);
        }
        return phaseAtViewActionMethod;
    }

    /**
     * Retrieve the default PhaseIdType defined by the phaseAtViewActionMethod in the annotation
     *
     * @param phaseAtViewActionMethod
     * @param annotation
     * @return PhaseIdType from the phaseAtViewActionMethod, null if empty
     */
    public PhaseIdType getViewActionPhaseId(Method phaseAtViewActionMethod, Annotation annotation) {
        PhaseIdType phaseId;
        try {
            phaseId = (PhaseIdType) phaseAtViewActionMethod.invoke(annotation);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("phase method must be accessible", ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
        return phaseId;
    }

    /**
     * Execute the list of applicable view action annotations, TODO...
     *
     * @param context
     * @param viewRoot
     * @param annotations
     */
    private void execute(FacesContext context, UIViewRoot viewRoot, List<? extends Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            log.debug("Annotations is null/empty");
            return;
        }
    }
}
