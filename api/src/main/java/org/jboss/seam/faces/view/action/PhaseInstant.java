package org.jboss.seam.faces.view.action;

import javax.faces.event.PhaseId;

/**
 * Identifies when the viewAction must take place in the JSF lifecycle.  
 */
public class PhaseInstant {
    private PhaseId phaseId;
    private boolean before;

    public static final PhaseInstant BEFORE_RENDER_RESPONSE = new PhaseInstant(PhaseId.RENDER_RESPONSE, true);
            
    public PhaseInstant(PhaseId phaseId, boolean before) {
        this.phaseId = phaseId;
        this.before = before;
    }

    public PhaseId getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(PhaseId phaseId) {
        this.phaseId = phaseId;
    }

    public boolean isBefore() {
        return before;
    }

    public void setBefore(boolean before) {
        this.before = before;
    }

    @Override
    public String toString() {
        return super.toString()+"{phase="+phaseId+",before"+before+"}";
    }

    @Override
    public int hashCode() {
        return phaseId.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PhaseInstant)) {
            return false;
        }
        PhaseInstant instant = (PhaseInstant) object;
        if (getPhaseId() != instant.getPhaseId()) {
            return false;
        }
        if (isBefore() != instant.isBefore()) {
            return false;
        }
        return true;
    }
}
