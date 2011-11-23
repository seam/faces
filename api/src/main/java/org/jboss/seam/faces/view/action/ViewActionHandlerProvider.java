package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This interface must be implemented by algorithms handling ViewAction annotations.
 * 
 * TODO : sample
 *  
 * @author Adriàn Gonzalez
 */
public interface ViewActionHandlerProvider<X extends Annotation> {
    /**
     * Returns list of handlers handling view actions for annotation X. 
     */
    List<ViewActionHandler> getActionHandlers();
    /**
     * Called during initialization.
     */
    void initialize(X annotation);
}
