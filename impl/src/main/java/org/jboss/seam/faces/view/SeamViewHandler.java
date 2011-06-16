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
