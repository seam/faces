package org.jboss.seam.faces.test.view.config.annotation;

import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.security.RestrictAtPhase;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

@ViewConfig
public interface ViewConfigEnum {
    static enum Pages {
        @ViewPattern("/*")
        @Icon("default.gif")
        DEFAULT,

        @ViewPattern("/happy/*")
        @Icon("happy.gif")
        HAPPY,

        @ViewPattern("/sad/*")
        @Icon("sad.gif")
        SAD(),

        @ViewPattern("/happy/done.xhtml")
        @Icon("finished.gif")
        HAPPY_DONE(),

        @ViewPattern("/qualified/*")
        @RestrictAtPhase(PhaseIdType.INVOKE_APPLICATION)
        @RestrictedDefault
        @QualifiedIcon("qualified.gif")
        QUALIFIED,

        @ViewPattern("/qualified/yes.xhtml")
        @RestrictedAtRestoreView
        QUALIFIED_YES;

    }
}
