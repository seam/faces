package org.jboss.seam.faces.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.reflection.AnnotationInspector;

/**
 * A utility providing &#064;Restrict manipulation functionnality.
 * 
 * Usefull for internal unit testing.
 */
public class RestrictViewActionUtils {

    private static final Logger log = Logger.getLogger(RestrictViewActionUtils.class);

    /**
     * Inspect an annotation to see if it specifies a view in which it should be. Fall back on default view otherwise.
     * 
     * @param annotation
     * @param currentPhase
     * @param defaultPhases
     * @param beanManager
     * @return true if the annotation is applicable to this view and phase, false otherwise
     */
    public static boolean isAnnotationApplicableToPhase(Annotation annotation, PhaseIdType currentPhase,
            PhaseIdType[] defaultPhases, BeanManager beanManager) {
        Method restrictAtViewMethod = getRestrictAtViewMethod(annotation);
        PhaseIdType[] phasedIds = null;
        if (restrictAtViewMethod != null) {
            log.warnf("Annotation %s is using the restrictAtViewMethod. Use a @RestrictAtPhase qualifier on the annotation instead.");
            phasedIds = getRestrictedPhaseIds(restrictAtViewMethod, annotation);
        }
        RestrictAtPhase restrictAtPhaseQualifier = AnnotationInspector.getAnnotation(annotation.annotationType(),
                RestrictAtPhase.class, beanManager);
        if (restrictAtPhaseQualifier != null) {
            log.debug("Using Phases found in @RestrictAtView qualifier on the annotation.");
            phasedIds = restrictAtPhaseQualifier.value();
        }
        if (phasedIds == null) {
            log.debug("Falling back on default phase ids");
            phasedIds = defaultPhases;
        }
        if (Arrays.binarySearch(phasedIds, currentPhase) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * Utility method to extract the "restrictAtPhase" method from an annotation
     * 
     * @param annotation
     * @return restrictAtViewMethod if found, null otherwise
     */
    public static Method getRestrictAtViewMethod(Annotation annotation) {
        Method restrictAtViewMethod;
        try {
            restrictAtViewMethod = annotation.annotationType().getDeclaredMethod("restrictAtPhase");
        } catch (NoSuchMethodException ex) {
            restrictAtViewMethod = null;
        } catch (SecurityException ex) {
            throw new IllegalArgumentException("restrictAtView method must be accessible", ex);
        }
        return restrictAtViewMethod;
    }

    /**
     * Retrieve the default PhaseIdTypes defined by the restrictAtViewMethod in the annotation
     * 
     * @param restrictAtViewMethod
     * @param annotation
     * @return PhaseIdTypes from the restrictAtViewMethod, null if empty
     */
    public static PhaseIdType[] getRestrictedPhaseIds(Method restrictAtViewMethod, Annotation annotation) {
        PhaseIdType[] phaseIds;
        try {
            phaseIds = (PhaseIdType[]) restrictAtViewMethod.invoke(annotation);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("restrictAtView method must be accessible", ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
        return phaseIds;
    }

    /**
     * Get the default phases at which restrictions should be applied, by looking for a @RestrictAtPhase on a matching
     * 
     * @param viewId
     * @param viewConfigStore
     * @return default phases for a view
     * @ViewPattern, falling back on global defaults if none are found
     */
    public static PhaseIdType[] getDefaultPhases(String viewId, ViewConfigStore viewConfigStore) {
        PhaseIdType[] defaultPhases = null;
        RestrictAtPhase restrictAtPhase = viewConfigStore.getAnnotationData(viewId, RestrictAtPhase.class);
        if (restrictAtPhase != null) {
            defaultPhases = restrictAtPhase.value();
        }
        if (defaultPhases == null) {
            defaultPhases = RestrictAtPhaseDefault.DEFAULT_PHASES;
        }
        return defaultPhases;
    }

}
