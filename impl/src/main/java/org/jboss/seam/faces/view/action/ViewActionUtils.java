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
            } else if (annotationType == ApplyRequestValues.class) {
                if (phaseId != null) {
                    throw new IllegalStateException("invalid " + parentElement
                            + ". Cannot be annotated simultaneously with multiples JSF lifecycle annotations ("
                            + annotationType + " and " + phaseId + ")");
                }
                phaseId = PhaseId.APPLY_REQUEST_VALUES;
            } else if (annotationType == ProcessValidations.class) {
                if (phaseId != null) {
                    throw new IllegalStateException("invalid " + parentElement
                            + ". Cannot be annotated simultaneously with multiples JSF lifecycle annotations ("
                            + annotationType + " and " + phaseId + ")");
                }
                phaseId = PhaseId.PROCESS_VALIDATIONS;
            } else if (annotationType == UpdateModelValues.class) {
                if (phaseId != null) {
                    throw new IllegalStateException("invalid " + parentElement
                            + ". Cannot be annotated simultaneously with multiples JSF lifecycle annotations ("
                            + annotationType + " and " + phaseId + ")");
                }
                phaseId = PhaseId.UPDATE_MODEL_VALUES;
            } else if (annotationType == InvokeApplication.class) {
                if (phaseId != null) {
                    throw new IllegalStateException("invalid " + parentElement
                            + ". Cannot be annotated simultaneously with multiples JSF lifecycle annotations ("
                            + annotationType + " and " + phaseId + ")");
                }
                phaseId = PhaseId.INVOKE_APPLICATION;
            } else if (annotationType == RenderResponse.class) {
                if (phaseId != null) {
                    throw new IllegalStateException("invalid " + parentElement
                            + ". Cannot be annotated simultaneously with multiples JSF lifecycle annotations ("
                            + annotationType + " and " + phaseId + ")");
                }
                phaseId = PhaseId.RENDER_RESPONSE;
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
}
