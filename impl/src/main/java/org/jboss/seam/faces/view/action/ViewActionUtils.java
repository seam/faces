package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;

public class ViewActionUtils {

    // Utility class - no instanciation
    private ViewActionUtils() {
    }

    public static PhaseInstant getPhaseInstantOrDefault(List<Annotation> annotations, Object parentElement,
            PhaseInstant defaultInstant) {
        PhaseInstant phaseInstant = getPhaseInstant(annotations, parentElement);
        return phaseInstant != null ? phaseInstant : defaultInstant;
    }

    public static PhaseInstant getPhaseInstant(List<Annotation> annotations, Object parentElement) {
        Boolean before = null;
        PhaseId phaseId = null;
        for (Annotation annotation : annotations) {
            Class<?> annotationType = annotation.annotationType();
            if (annotationType == Before.class) {
                if (before != null) {
                    throw new IllegalStateException("invalid " + parentElement
                            + ". Cannot be annotated simultaneously with multiples @Before and @After");
                }
                before = true;
            } else if (annotationType == After.class) {
                if (before != null) {
                    throw new IllegalStateException("invalid " + parentElement
                            + ". Cannot be annotated simultaneously with multiples @Before and @After");
                }
                before = false;
            } else if (isPhaseQualifier(annotationType)) {
                if (phaseId != null) {
                    throw new IllegalStateException("invalid " + parentElement
                            + ". Cannot be annotated simultaneously with multiples JSF lifecycle annotations ("
                            + annotationType + " and " + phaseId + ")");
                }
                phaseId = convert(annotationType);
            }
        }
        if (before == null && phaseId == null) {
            return null;
        } else if (before != null && phaseId != null) {
            return new PhaseInstant(phaseId, before);
        } else {
            throw new IllegalStateException("invalid " + parentElement
                    + ". both phaseId and @Before/@After must be specified {phaseId: " + phaseId + ", before: " + before + "}");
        }
    }

    /**
     * Converts the annotations from package org.jboss.seam.faces.event.qualifier to their corresponding JSF PhaseId.
     * 
     * @throws IllegalArgumentException if annotationType isn't a valid Jsf annotation.
     */
    public static PhaseId convert(Class<?> annotationType) {
        PhaseId phaseId;
        if (annotationType == ApplyRequestValues.class) {
            phaseId = PhaseId.APPLY_REQUEST_VALUES;
        } else if (annotationType == ProcessValidations.class) {
            phaseId = PhaseId.PROCESS_VALIDATIONS;
        } else if (annotationType == UpdateModelValues.class) {
            phaseId = PhaseId.UPDATE_MODEL_VALUES;
        } else if (annotationType == InvokeApplication.class) {
            phaseId = PhaseId.INVOKE_APPLICATION;
        } else if (annotationType == RenderResponse.class) {
            phaseId = PhaseId.RENDER_RESPONSE;
        } else {
            throw new IllegalArgumentException("Annotation " + annotationType + " doesn't correspond to valid a Jsf phase.");
        }
        return phaseId;
    }

    /**
     * Returns true if annotationType is a valid JSF annotation
     */
    public static boolean isPhaseQualifier(Class<?> annotationType) {
        if (annotationType == ApplyRequestValues.class || annotationType == ProcessValidations.class
                || annotationType == UpdateModelValues.class || annotationType == InvokeApplication.class
                || annotationType == RenderResponse.class) {
            return true;
        } else {
            return false;
        }
    }
}
