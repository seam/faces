package org.jboss.seam.faces.lifecycle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * A class that is invoked by the JCDI Manager handles setup and cleanup tasks
 * which are specific to Seam's JSF support. This listener does not tasks which
 * are relevant for any servlet request. Specifically, it raises events so that
 * other beans managed by JCDI can tie into the JSF phases and it handles
 * transaction management a key points in the JSF life cycle.
 * 
 * @author Dan Allen
 */
public
@ApplicationScoped
class ManagedSeamPhaseListener
{
   private BeanManager manager;
   
   private UserTransaction transaction;
   
   public ManagedSeamPhaseListener() {}
   
   public @Inject ManagedSeamPhaseListener(BeanManager manager, 
          UserTransaction transaction)
   {
      this.manager = manager;
      this.transaction = transaction;
   }
   
   public void beforePhase(PhaseEvent event)
   {
      handleTransactionBeforePhase(event);
      raiseEventBeforePhase(event);
   }
   
   public void afterPhase(PhaseEvent event)
   {
      handleTransactionAfterPhase(event);
      raiseEventAfterPhase(event);
   }
   
   protected void handleTransactionBeforePhase(PhaseEvent event)
   {
      if (event.getPhaseId() == PhaseId.RENDER_RESPONSE)
      {
         beginTransaction(event.getPhaseId());
      }
   }
   
   protected void handleTransactionAfterPhase(PhaseEvent event)
   {
      if (event.getPhaseId() == PhaseId.RENDER_RESPONSE)
      {
         commitOrRollbackTransaction(event.getPhaseId());
      }
   }
   
   protected void beginTransaction(PhaseId phase)
   {
      try
      {
         int status = transaction.getStatus();
         if (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK)
         {
            transaction.begin();
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not begin transaction", e);
      }
   }
   
   protected void commitOrRollbackTransaction(PhaseId phase)
   {
      try
      {
         int status = transaction.getStatus();
         if (status == Status.STATUS_ACTIVE)
         {
            transaction.commit();
         }
         else if (status == Status.STATUS_MARKED_ROLLBACK)
         {
            transaction.rollback();
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not commit or rollback transaction", e);
      }
   }
   
   protected void raiseEventBeforePhase(PhaseEvent event)
   {
      manager.fireEvent(event, new AnnotationLiteral<BeforePhase>() {});
   }
   
   protected void raiseEventAfterPhase(PhaseEvent event)
   {
      manager.fireEvent(event, new AnnotationLiteral<AfterPhase>() {});
   }
}
