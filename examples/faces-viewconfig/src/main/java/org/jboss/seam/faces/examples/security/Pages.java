package org.jboss.seam.faces.examples.security;

import org.jboss.seam.faces.rewrite.FacesRedirect;
import org.jboss.seam.faces.rewrite.UrlMapping;
import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

/**
 *
 * @author bleathem
 */
@ViewConfig
public interface Pages {

    static enum Pages1 {

        @FacesRedirect
        @ViewPattern("/*")
        @AccessDeniedView("/item/list.xhtml")
        @LoginView("/login.xhtml")
        ALL,
        
        @UrlMapping(pattern="/index.asd")
        @ViewPattern("/index.xhtml")
        INDEX,
        
        @ViewPattern("/status.xhtml")
        @Public
        STATUS,
        
        @ViewPattern("/item.xhtml")
        @Owner
        ITEM;
    }
}
