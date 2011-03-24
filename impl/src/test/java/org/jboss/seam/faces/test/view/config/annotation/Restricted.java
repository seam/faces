package org.jboss.seam.faces.test.view.config.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.security.annotations.SecurityBindingType;

@SecurityBindingType
@Retention(RetentionPolicy.RUNTIME)
public @interface Restricted {
    public PhaseIdType[] restrictAtPhase();
}
