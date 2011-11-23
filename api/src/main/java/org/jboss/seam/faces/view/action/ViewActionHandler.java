package org.jboss.seam.faces.view.action;

/**
 * This handler encapsulate an action executed during a jsf view processing.
 * 
 * @author Adriàn Gonzalez
 */
public interface ViewActionHandler {

    /**
     * Returns true if this action must be executed on phaseInstant JSF lifecycle.
     * 
     * <p>The returned value should never change during a viewActionHandler life instance.</p>
     */
    boolean handles(PhaseInstant phaseInstant);

    /**
     * Returns execution order.
     * 
     * View actions will be executed from lower to higher precedence.
     * This method can return null. In this case, this action will be the last one to be called.  
     * Default : null.
     */
    Integer getOrder();

    /**
     * View action execution.
     */
    Object execute();
}
