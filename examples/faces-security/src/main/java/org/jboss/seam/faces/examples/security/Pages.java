/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.seam.faces.examples.security;

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

        @ViewPattern("/*")
        @AccessDeniedView("/item/list.xhtml")
        @LoginView("/login.xhtml")
        ALL,
        
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
