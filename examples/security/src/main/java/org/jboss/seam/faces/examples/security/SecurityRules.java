package org.jboss.seam.faces.examples.security;

import javax.inject.Inject;
import org.jboss.logging.Logger;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;

/**
 *
 * @author bleathem
 */
public class SecurityRules {

    private transient final Logger logger = Logger.getLogger(SecurityRules.class.getName());

    public @Secures @Owner boolean ownerChecker(Identity identity) {
        if (identity == null || identity.getUser() == null) {
            logger.info("Identity/User is null");
            return false;
        } else {
            String id = identity.getUser().getId();
            logger.infof("Username is: %s", id);
            return "demo".equals(id);
        }
    }

    public @Secures @Public boolean publicChecker() {
        return true;
    }
}
