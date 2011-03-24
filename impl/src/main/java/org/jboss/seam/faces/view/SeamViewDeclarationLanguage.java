package org.jboss.seam.faces.view;

import java.beans.BeanInfo;
import java.io.IOException;

import javax.faces.application.Resource;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.StateManagementStrategy;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewMetadata;

/**
 * Wrap the built-in {@link ViewDeclarationLanguage} implementation for the purpose of wrapping the {@link ViewMetadata}
 * implementation in {@link SeamViewMetadata}.
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 * @see SeamViewMetadata
 */
public class SeamViewDeclarationLanguage extends ViewDeclarationLanguage {
    private ViewDeclarationLanguage delegate;

    public SeamViewDeclarationLanguage(ViewDeclarationLanguage delegate) {
        this.delegate = delegate;
    }

    @Override
    public BeanInfo getComponentMetadata(FacesContext context, Resource componentResource) {
        return delegate.getComponentMetadata(context, componentResource);
    }

    /**
     * If non-null, wrap the {@link ViewMetadata} returned by the delegate in a new instance of {@link SeamViewMetadata}.
     */
    @Override
    public ViewMetadata getViewMetadata(FacesContext context, String viewId) {
        ViewMetadata metadata = delegate.getViewMetadata(context, viewId);
        if (metadata != null) {
            return new SeamViewMetadata(metadata);
        } else {
            return null;
        }
    }

    @Override
    public Resource getScriptComponentResource(FacesContext context, Resource componentResource) {
        return delegate.getScriptComponentResource(context, componentResource);
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        return delegate.createView(context, viewId);
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return delegate.restoreView(context, viewId);
    }

    @Override
    public void buildView(FacesContext context, UIViewRoot root) throws IOException {
        delegate.buildView(context, root);
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot view) throws IOException {
        delegate.renderView(context, view);
    }

    @Override
    public StateManagementStrategy getStateManagementStrategy(FacesContext context, String viewId) {
        return delegate.getStateManagementStrategy(context, viewId);
    }

}
