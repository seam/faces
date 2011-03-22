/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Event thrown to request an authorization check based on the given set of 
 * Seam Security annotations
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class SecurityCheckEvent {
    private final List<? extends Annotation> securityBindingTypes;
    private boolean authorized;

    public SecurityCheckEvent(List<? extends Annotation> securityBindingTypes) {
        this.securityBindingTypes = securityBindingTypes;
    }

    public List<? extends Annotation> getSecurityBindingTypes() {
        return securityBindingTypes;
    }
    
    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

}
