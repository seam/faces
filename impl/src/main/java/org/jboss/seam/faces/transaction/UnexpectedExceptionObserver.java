/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.faces.transaction;

import javax.enterprise.event.Observes;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.inject.Inject;
import javax.transaction.SystemException;

import org.jboss.seam.transaction.DefaultTransaction;
import org.jboss.seam.transaction.SeamTransaction;
import org.jboss.solder.logging.Logger;

/**
 * Simple observer to mark the current transaction for rollback when an unexpected exception occurs.
 * NOTE: Must be using Seam Transaction for this to work properly, otherwise it's skipped.
 *
 * @author <a href="http://community.jboss.org/people/LightGuard">Jason Porter</a>
 */
public class UnexpectedExceptionObserver {

    @Inject
    private Logger log;

    @Inject
    @DefaultTransaction
    private SeamTransaction tx;

    public void processEvent(@Observes ExceptionQueuedEvent event) throws AbortProcessingException {
        try {
            log.debug("Marking transaction for rollback based on unexpected exception", event.getContext().getException());
            tx.setRollbackOnly();
        } catch (SystemException e) {
            log.warn("Could not mark transaction for rollback", e);
        }
    }
}
