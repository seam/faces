package org.jboss.seam.faces.context;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

/**
 * An extension to provide {@link RenderScoped} CDI / JSF 2 integration.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class RenderScopedExtension implements Extension {

    public void addScope(@Observes final BeforeBeanDiscovery event) {
        event.addScope(RenderScoped.class, true, true);
    }

    public void registerContext(@Observes final AfterBeanDiscovery event) {
        event.addContext(new RenderScopedContext());
    }

}
