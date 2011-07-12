package org.jboss.seam.faces.component;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author <a href="http://community.jboss.org/people/bleathem)">Brian Leathem</a>
 */
@RequestScoped
@Named
public class ViewActionTestBean {
    public String action() {
        return "/result";
    }
}
