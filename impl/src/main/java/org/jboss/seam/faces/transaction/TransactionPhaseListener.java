/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.faces.transaction;

import static javax.faces.event.PhaseId.ANY_PHASE;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.viewdata.ViewDataStore;
import org.jboss.seam.persistence.PersistenceContexts;
import org.jboss.seam.persistence.transaction.DefaultTransaction;
import org.jboss.seam.persistence.transaction.SeamTransaction;

/**
 * Phase listener that is responsible for seam managed transactions. It is also
 * responsible for setting the correct flush mode on the persistence context
 * during the render response phase
 * 
 * @author Stuart Douglas
 * 
 */
public class TransactionPhaseListener implements PhaseListener
{
   private static final long serialVersionUID = -9127555729455066493L;

   private static final Logger log = Logger.getLogger(TransactionPhaseListener.class);

   @Inject
   @DefaultTransaction
   SeamTransaction transaction;

   @Inject
   ViewDataStore dataStore;

   @Inject
   PersistenceContexts persistenceContexts;

   public PhaseId getPhaseId()
   {
      return ANY_PHASE;
   }

   public void beforePhase(PhaseEvent event)
   {
      log.trace("before phase: " + event.getPhaseId());
      handleTransactionsBeforePhase(event);
   }

   public void afterPhase(PhaseEvent event)
   {
      if (event.getPhaseId() == RENDER_RESPONSE)
      {
         persistenceContexts.afterRender();
      }
      handleTransactionsAfterPhase(event);
   }

   public void handleTransactionsBeforePhase(PhaseEvent event)
   {
      PhaseId phaseId = event.getPhaseId();
      if (seamManagedTransactionStatus(phaseId))
      {
         if (phaseId == RENDER_RESPONSE)
         {
            persistenceContexts.beforeRender();
         }
         boolean beginTran = ((phaseId == PhaseId.RENDER_RESPONSE) || (phaseId == PhaseId.RESTORE_VIEW));
         if (beginTran)
         {
            begin(phaseId);
         }
      }
   }

   public void handleTransactionsAfterPhase(PhaseEvent event)
   {
      PhaseId phaseId = event.getPhaseId();
      if (seamManagedTransactionStatus(phaseId))
      {
         boolean commitTran = (phaseId == PhaseId.INVOKE_APPLICATION) || event.getFacesContext().getRenderResponse() ||
               event.getFacesContext().getResponseComplete() || (phaseId == PhaseId.RENDER_RESPONSE);

         if (commitTran)
         {
            commitOrRollback(phaseId); // we commit before destroying contexts,
                                       // cos the contexts have the PC in them
         }
      }
   }

   void begin(PhaseId phaseId)
   {
      begin("prior to phase: " + phaseId);
   }

   void begin(String phaseString)
   {
      try
      {
         if (!transaction.isActiveOrMarkedRollback())
         {
            log.debug("beginning transaction " + phaseString);
            transaction.begin();
         }
      }
      catch (Exception e)
      {
         throw new IllegalStateException("Could not start transaction", e);
      }
   }

   void commitOrRollback(PhaseId phaseId)
   {
      commitOrRollback("after phase: " + phaseId);
   }

   void commitOrRollback(String phaseString)
   {
      try
      {
         if (transaction.isActive())
         {
            try
            {
               log.debug("committing transaction " + phaseString);
               transaction.commit();

            }
            catch (IllegalStateException e)
            {
               log.warn("TX commit failed with illegal state exception. This may be because the tx timed out and was rolled back in the background.", e);
            }
         }
         else if (transaction.isRolledBackOrMarkedRollback())
         {
            log.debug("rolling back transaction " + phaseString);
            transaction.rollback();
         }

      }
      catch (Exception e)
      {
         throw new IllegalStateException("Could not commit transaction", e);
      }
   }

   private boolean seamManagedTransactionStatus(PhaseId phase)
   {
      SeamManagedTransaction an = dataStore.getDataForCurrentViewId(SeamManagedTransaction.class);
      SeamManagedTransactionType config;
      if (an == null)
      {
         // enable seam managed transactions by default
         config = SeamManagedTransactionType.ENABLED;
      }
      else
      {
         config = an.value();
      }
      if (config == SeamManagedTransactionType.DISABLED)
      {
         return false;
      }
      else if ((config == SeamManagedTransactionType.RENDER_RESPONSE) && (phase != PhaseId.RENDER_RESPONSE))
      {
         return false;
      }
      return true;
   }
}
