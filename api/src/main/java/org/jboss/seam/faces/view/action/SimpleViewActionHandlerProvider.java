package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation for ViewActionHandlerProvider returning a single ViewActionHandler (view action 'injection points'
 * executing a single action).
 * 
 * @author Adriàn Gonzalez
 */
public abstract class SimpleViewActionHandlerProvider<X extends Annotation> extends ViewActionHandlerProviderSupport<X>
        implements ViewActionHandler {

    private List<ViewActionHandler> actionHandlers;

    public SimpleViewActionHandlerProvider() {
        actionHandlers = new ArrayList<ViewActionHandler>();
        actionHandlers.add(this);
    }

    @Override
    public List<ViewActionHandler> getActionHandlers() {
        return actionHandlers;
    }

    @Override
    public boolean handles(PhaseInstant phaseInstant) {
        return phaseInstant.equals(getPhaseInstant());
    }

    @Override
    public abstract Object execute();

}
