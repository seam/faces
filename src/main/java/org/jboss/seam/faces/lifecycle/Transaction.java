package org.jboss.seam.faces.lifecycle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

// TEMPORARY!!
public class Transaction
{
   private static final String USER_TRANSACTION_JNDI_NAME = "java:comp/UserTransaction";
   
   // Embedded JBoss has no java:comp/UserTransaction
   private static final String ALTERNATE_USER_TRANSACTION_JNDI_NAME = "UserTransaction";
   
   public
   //@Produces
   @RequestScoped
   UserTransaction getUserTransaction() throws NamingException
   {
      InitialContext context = new InitialContext();
      try
      {
         try
         {
            return (UserTransaction) context.lookup(USER_TRANSACTION_JNDI_NAME);
         }
         catch (NameNotFoundException nnfe)
         {
            try
            {
               return (UserTransaction) context.lookup(ALTERNATE_USER_TRANSACTION_JNDI_NAME);
            }
            catch (NamingException e)
            {
               throw nnfe;
            }
         }
      }
      catch (NamingException e)
      {
         throw e;
      }
   }
}
