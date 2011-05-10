package org.jboss.seam.faces.event;

/**
 * Enum values corresponding to the phases of the JSF life-cycle.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public enum PhaseIdType {
    ANY_PHASE, RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, UPDATE_MODEL_VALUES, INVOKE_APPLICATION, RENDER_RESPONSE;
}
