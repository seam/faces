/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.seam.faces.scopes;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author lbaxter
 */
public class ViewScopedExtension implements Extension {

    public void addScope(@Observes BeforeBeanDiscovery event)
    {
        event.addScope(ViewScoped.class, true, true);
    }

    public void registerContext(@Observes AfterBeanDiscovery event)
    {
        event.addContext(new ViewScopedContext());
    }

}
