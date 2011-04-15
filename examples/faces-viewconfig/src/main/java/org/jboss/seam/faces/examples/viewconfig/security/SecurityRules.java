package org.jboss.seam.faces.examples.viewconfig.security;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.examples.viewconfig.model.Current;
import org.jboss.seam.faces.examples.viewconfig.model.Item;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;
import org.picketlink.idm.api.User;

/**
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class SecurityRules {
    public @Secures @Owner boolean ownerChecker(Identity identity, @Current Item item) {
        if (item == null || identity.getUser() == null) {
            return false;
        } else {
            return item.getOwner().equals(identity.getUser().getId());
        }
    }

    public @Secures @Admin boolean adminChecker(Identity identity) {
        if (identity.getUser() == null) {
            return false;
        } else {
            return "admin".equals(identity.getUser().getId());
        }
    }
}
