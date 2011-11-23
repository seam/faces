package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;
import org.jboss.solder.reflection.Reflections;

public class ViewActionHandlerUtils {

    // Utility class - no instanciation
    private ViewActionHandlerUtils() {
    }

    /**
     * @see {@link #getPhaseInstant(Annotation)}
     */
    public static PhaseInstant getPhaseInstantOrDefault(Annotation annotation, PhaseInstant defaultInstant) {
        PhaseInstant phaseInstant = getPhaseInstant(annotation);
        return phaseInstant != null ? phaseInstant : defaultInstant;
    }

    /**
     * Returns phaseInstant for the given annotation.
     * 
     * phaseInstant is calculated from (in order of priority) :
     * <ul>
     * <li>attribute before (type boolean).</li>
     * <li>attribute phase (type <code>ApplyRequestValues</code>, <code>ProcessValidations</code>, etc...)</li>
     * <li>annotation Before or After on annotation.</li>
     * <li>annotation phase (type <code>ApplyRequestValues</code>, <code>ProcessValidations</code>, etc...) on annotation.</li>
     */
    public static PhaseInstant getPhaseInstant(Annotation annotation) {
        PhaseInstant phaseInstant = getPhaseInstantValueFromAttribute(annotation);
        if (phaseInstant == null) {
            phaseInstant = getPhaseInstant(Arrays.asList(annotation.annotationType().getAnnotations()), annotation);
        }
        return phaseInstant;
    }

    /**
     * If annotation has phase or before attribute, returns a corresponding PhaseInstant. If no such attributes exists, returns
     * null.
     * 
     * @param annotation
     * @return
     * @exception IllegalArgumentException If one of those annotations exists but doesn't correspond to required types.
     */
    private static PhaseInstant getPhaseInstantValueFromAttribute(Annotation annotation) {
        Class<?> phase = attributeValue("phase", Class.class, annotation);
        Boolean before = attributeValue("before", Boolean.class, annotation);
        if (phase != null || before != null) {
            PhaseId phaseId = phase != null ? convert(phase) : PhaseId.RENDER_RESPONSE;
            return new PhaseInstant(phaseId, (before != null ? before : true));
        } else {
            return null;
        }
    }
    
    private static <E> E attributeValue (String attribute, Class<E> expectedType, Annotation annotation) {
        Class<?> clazz = annotation.annotationType();
        try {
            Method method = Reflections.findDeclaredMethod(clazz, attribute, (Class<?>[]) null);
            if (method != null) {
                return Reflections.invokeMethod(method, expectedType, annotation);
            } else {
                return null;
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("invalid annotation " + annotation
                    + " : should return Class type (i.e. phase=InvokeApplication.class)");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("invalid annotation " + annotation + " : error accessing " + attribute
                    + " attribute : " + e.toString(), e);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("invalid annotation " + annotation + " : error accessing " + attribute
                    + " attribute : " + e.toString(), e);
        }
    }

    public static PhaseInstant getPhaseInstantOrDefault(List<Annotation> annotations, Object parentElement,
            PhaseInstant defaultInstant) {
        PhaseInstant phaseInstant = getPhaseInstant(annotations, parentElement);
        return phaseInstant != null ? phaseInstant : defaultInstant;
    }

    /**
     * Returns phaseInstant for the given annotation.
     * 
     * phaseInstant is calculated from (in order of priority) :
     * <ul>
     * <li>attribute before (type boolean).</li>
     * <li>attribute phase (type <code>ApplyRequestValues</code>, <code>ProcessValidations</code>, etc...)</li>
     * <li>annotation Before or After on annotation.</li>
     * <li>annotation phase (type <code>ApplyRequestValues</code>, <code>ProcessValidations</code>, etc...) on annotation.</li>
     */
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

    /**
     * Returns phaseInstant for the given annotation.
     * 
     * phaseInstant is calculated from (in order of priority) :
     * <ul>
     * <li>attribute before (type boolean).</li>
     * <li>attribute phase (type <code>ApplyRequestValues</code>, <code>ProcessValidations</code>, etc...)</li>
     * <li>annotation Before or After on annotation.</li>
     * <li>annotation phase (type <code>ApplyRequestValues</code>, <code>ProcessValidations</code>, etc...) on annotation.</li>
     */
    public static Integer getOrder(Annotation annotation) {
        Integer orderAnnotation = attributeValue("order", Integer.class, annotation);
        if (orderAnnotation != null) {
            return orderAnnotation;
        }
        Order order = annotation.annotationType().getAnnotation(Order.class);
        if (order != null) {
            return order.value();
        }
        return null;
    }
}
