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
package org.jboss.seam.faces.navigation;

import javax.enterprise.event.Observes;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.seam.faces.event.PreNavigateEvent;
import org.jboss.seam.faces.rewrite.FacesRedirect;
import org.jboss.seam.faces.view.config.ViewConfigStore;

/**
 * Intercept JSF navigations, and check for @FacesRedirect in the @ViewConfig.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class FacesRedirectConfiguration {
    @Inject
    private ViewConfigStore store;

    public void observePreNavigateEvent(@Observes PreNavigateEvent event) {
        FacesContext facesContext = event.getContext();
        NavigationCase navCase = event.getNavigationCase();
        if (navCase == null) {
            return;
        }
        String viewId = navCase.getToViewId(facesContext);
        FacesRedirect facesRedirect = store.getAnnotationData(viewId, FacesRedirect.class);
        if (facesRedirect == null || facesRedirect.value() == navCase.isRedirect()) {
            return;
        }
        String newOutcome = viewId;
        if (facesRedirect.value()) {
            newOutcome = newOutcome + "?faces-redirect=true";
        }
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, event.getFromAction(), newOutcome);
    }
}
