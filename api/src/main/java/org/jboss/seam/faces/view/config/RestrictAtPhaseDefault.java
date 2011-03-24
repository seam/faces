package org.jboss.seam.faces.view.config;

import org.jboss.seam.faces.event.PhaseIdType;

/**
 * The Default values for RestrictAtPhase annotation, extracted as constants.
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class RestrictAtPhaseDefault {
    public static final PhaseIdType RESTRICT_INITIAL_DEFAULT;
    public static final PhaseIdType RESTRICT_POSTBACK_DEFAULT;

    static {
        try {
            RESTRICT_INITIAL_DEFAULT = (PhaseIdType) RestrictAtPhase.class.getMethod("initial").getDefaultValue();
            RESTRICT_POSTBACK_DEFAULT = (PhaseIdType) RestrictAtPhase.class.getMethod("postback").getDefaultValue();
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Error initialising values", ex);
        } catch (SecurityException ex) {
            throw new IllegalStateException("Error initialising values", ex);
        }
    }
}
