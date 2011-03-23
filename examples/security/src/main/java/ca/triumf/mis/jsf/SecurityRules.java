package ca.triumf.mis.jsf;

import org.jboss.seam.security.annotations.Secures;

/**
 *
 * @author bleathem
 */
public class SecurityRules {
   public @Secures @Owner boolean ownerChecker() {
       return false;
   }
   
   public @Secures @Public boolean publicChecker() {
       return true;
   }
}
