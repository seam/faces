package org.jboss.seam.faces.test.weld.view.action.el;

import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.view.action.el.ElViewAction;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

@ViewConfig
public interface ElViewActionConfigEnum {
    static enum Pages {
        @ViewPattern("/*")
        DEFAULT,

        @ViewPattern("/client/*")
        @ElViewAction("#{elViewActionBean.viewAction}")
        CLIENTS,

        @ViewPattern("/client/done.xhtml")
        CLIENT_CONFIRMED(),

        @ViewPattern("/country/*")
        @ElViewAction("#{elViewActionBean.parameterizedViewAction('COUNTRIES')}")
        COUNTRIES(),

        @ViewPattern("/country/done.xhtml")
        @ElViewAction("#{elViewActionBean.parameterizedViewAction('COUNTRY_CONFIRMED')}")
        COUNTRY_CONFIRMED(),

        @ViewPattern("/explicit-phase/*")
        @ElViewAction(value="#{elViewActionBean.viewAction}", phase=InvokeApplication.class)
        EXPLICIT_PHASE(),

        @ViewPattern("/qualified/*")
        QUALIFIED,

        @ViewPattern("/qualified/yes.xhtml")
        QUALIFIED_YES;

    }
}
