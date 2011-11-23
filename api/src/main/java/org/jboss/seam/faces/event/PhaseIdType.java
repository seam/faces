/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.faces.event;

import javax.faces.event.PhaseId;

/**
 * Enum values corresponding to the phases of the JSF life-cycle.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public enum PhaseIdType {
    ANY_PHASE, RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, UPDATE_MODEL_VALUES, INVOKE_APPLICATION, RENDER_RESPONSE;
    
    public static PhaseId convert(PhaseIdType phaseIdType) {
        switch (phaseIdType) {
            case RESTORE_VIEW :
                return PhaseId.RESTORE_VIEW;
            case APPLY_REQUEST_VALUES :
                return PhaseId.APPLY_REQUEST_VALUES;
            case PROCESS_VALIDATIONS :
                return PhaseId.PROCESS_VALIDATIONS;
            case UPDATE_MODEL_VALUES :
                return PhaseId.UPDATE_MODEL_VALUES;
            case INVOKE_APPLICATION :
                return PhaseId.INVOKE_APPLICATION;
            case RENDER_RESPONSE :
                return PhaseId.RENDER_RESPONSE;
            case ANY_PHASE :
                return PhaseId.ANY_PHASE;
            default :
                throw new IllegalArgumentException ("Couldn't convert "+phaseIdType+" to JSF phase Id");
        }
    }
    
    public static PhaseIdType convert(PhaseId phaseId) {
        if (PhaseId.RESTORE_VIEW.equals(phaseId)) {
            return RESTORE_VIEW;
        }
        if (PhaseId.APPLY_REQUEST_VALUES.equals(phaseId)) {
            return APPLY_REQUEST_VALUES;
        }
        if (PhaseId.PROCESS_VALIDATIONS.equals(phaseId)) {
            return PROCESS_VALIDATIONS;
        }
        if (PhaseId.UPDATE_MODEL_VALUES.equals(phaseId)) {
            return UPDATE_MODEL_VALUES;
        }
        if (PhaseId.INVOKE_APPLICATION.equals(phaseId)) {
            return INVOKE_APPLICATION;
        }
        if (PhaseId.RENDER_RESPONSE.equals(phaseId)) {
            return RENDER_RESPONSE;
        }
        if (PhaseId.ANY_PHASE.equals(phaseId)) {
            return ANY_PHASE;
        }
        throw new IllegalArgumentException ("Couldn't convert "+phaseId+" to JSF phase Id");
    }
}
