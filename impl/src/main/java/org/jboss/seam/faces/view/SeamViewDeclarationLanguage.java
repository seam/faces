/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.faces.view;

import java.beans.BeanInfo;
import java.io.IOException;
import java.util.List;

import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectHandler;
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

    @Override
    public void retargetAttachedObjects(FacesContext context, UIComponent topLevelComponent,
            List<AttachedObjectHandler> handlers) {
        delegate.retargetAttachedObjects(context, topLevelComponent, handlers);
    }

    @Override
    public void retargetMethodExpressions(FacesContext context, UIComponent topLevelComponent) {
        delegate.retargetMethodExpressions(context, topLevelComponent);
    }

}
