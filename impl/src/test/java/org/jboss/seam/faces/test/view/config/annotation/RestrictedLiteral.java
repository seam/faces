package org.jboss.seam.faces.test.view.config.annotation;

import javax.enterprise.util.AnnotationLiteral;
import org.jboss.seam.faces.event.PhaseIdType;

public class RestrictedLiteral extends AnnotationLiteral<Restricted> implements Restricted {
    private final PhaseIdType[] restrictAtPhase;

    public RestrictedLiteral() {
        this.restrictAtPhase = null;
    }
    
    public RestrictedLiteral(PhaseIdType restrictAtPhase) {
        if (restrictAtPhase != null) {
            this.restrictAtPhase = new PhaseIdType[] {restrictAtPhase};
        }
        else {
            this.restrictAtPhase = null;
        }
    }
    
    public RestrictedLiteral(PhaseIdType[] restrictAtPhase) {
        this.restrictAtPhase = restrictAtPhase;
    }

    @Override
    public PhaseIdType[] restrictAtPhase() {
        return restrictAtPhase;
    }


}
