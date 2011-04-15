package org.jboss.seam.faces.examples.viewconfig;

import org.jboss.seam.faces.examples.viewconfig.security.Admin;
import org.jboss.seam.faces.examples.viewconfig.security.Owner;
import org.jboss.seam.faces.rewrite.FacesRedirect;
import org.jboss.seam.faces.rewrite.UrlMapping;
import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

/**
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@ViewConfig
public interface Pages {

    static enum Pages1 {

        @FacesRedirect
        @ViewPattern("/*")
        @AccessDeniedView("/denied.xhtml")
        @LoginView("/login.xhtml")
        ALL,
        
        
        @ViewPattern("/index.xhtml")
        INDEX,
        
        @ViewPattern("/list.xhtml")
        LIST,
        
        @ViewPattern("/admin.xhtml")
        @Admin
        ADMIN,
        
        @UrlMapping(pattern="/item/#{item}/")
        @ViewPattern("/item.xhtml")
        @Owner
        ITEM;
    }
}
