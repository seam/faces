package org.jboss.seam.faces.view.action;

import org.jboss.seam.faces.event.PhaseIdType;

/**
 * The Default values for Phase annotation, extracted as constant.
 *
 * @author Adriàn Gonzalez
 */
public class PhaseDefault {
    public static final PhaseIdType DEFAULT_PHASE;

    static {
        try {
            DEFAULT_PHASE = (PhaseIdType) Phase.class.getMethod("value").getDefaultValue();
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Error initialising values", ex);
        } catch (SecurityException ex) {
            throw new IllegalStateException("Error initialising values", ex);
        }
    }
}
