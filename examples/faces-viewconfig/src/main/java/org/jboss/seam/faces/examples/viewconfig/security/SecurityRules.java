package org.jboss.seam.faces.examples.viewconfig.security;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.examples.viewconfig.model.Current;
import org.jboss.seam.faces.examples.viewconfig.model.Item;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;

/**
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class SecurityRules {

    private transient final Logger logger = Logger.getLogger(SecurityRules.class.getName());

    public @Secures @Owner boolean ownerChecker(Identity identity, @Current Item item) {
        if (item == null) {
            logger.info("Item is null");
            return false;
        } else {
            return doesUsernameMatch(identity, item.getOwner());
        }
    }

    public @Secures @Public boolean publicChecker() {
        return true;
    }
    
    public @Secures @Admin boolean adminChecker(Identity identity) {
        return doesUsernameMatch(identity, "admin");
    }
    
    private boolean doesUsernameMatch(Identity identity, String expectedUserName) {
        if (identity == null || identity.getUser() == null) {
            logger.info("Identity/User is null");
            return false;
        } else {
            String username = identity.getUser().getId();
            logger.infof("Username is: %s", username);
            return username.equals(expectedUserName);
        }
    }
}
