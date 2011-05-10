package org.jboss.seam.faces.transaction;

/**
 * @author Stuart Douglas
 */
public enum SeamManagedTransactionType {

    ENABLED, DISABLED,
    /**
     * Transactions are only enabled during the RENDER_RESPONSE phase
     */
    RENDER_RESPONSE;
}
