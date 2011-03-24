package org.jboss.seam.faces.view;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

/**
 * Wrap the built-in {@link ViewHandler} implementation for the purpose of wrapping the {@link ViewDeclarationLanguage}
 * implementation in {@link SeamViewDeclarationLanguage}.
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 * @see SeamViewDeclarationLanguage
 */
public class SeamViewHandler extends ViewHandlerWrapper {
    private ViewHandler delegate;

    public SeamViewHandler(ViewHandler delegate) {
        this.delegate = delegate;
    }

    /**
     * If non-null, wrap the {@link ViewDeclarationLanguage} returned by the delegate in a new
     * {@link SeamViewDeclarationLanguage} instance.
     */
    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context, String viewId) {
        ViewDeclarationLanguage vdl = delegate.getViewDeclarationLanguage(context, viewId);
        if (vdl != null) {
            return new SeamViewDeclarationLanguage(vdl);
        } else {
            return null;
        }
    }

    @Override
    public ViewHandler getWrapped() {
        return delegate;
    }
}
