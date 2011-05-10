package org.jboss.seam.faces.security;

import org.jboss.seam.faces.event.PhaseIdType;

/**
 * The Default values for RestrictAtPhase annotation, extracted as constants.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class RestrictAtPhaseDefault {
    public static final PhaseIdType[] DEFAULT_PHASES;

    static {
        try {
            DEFAULT_PHASES = (PhaseIdType[]) RestrictAtPhase.class.getMethod("value").getDefaultValue();
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Error initialising values", ex);
        } catch (SecurityException ex) {
            throw new IllegalStateException("Error initialising values", ex);
        }
    }
}
