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
package org.jboss.seam.faces.event;

import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import org.jboss.seam.solder.beanManager.BeanManagerLocator;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
public class SeamPreNavigationHandler extends ConfigurableNavigationHandler {
    private final ConfigurableNavigationHandler parent;

    public SeamPreNavigationHandler(final ConfigurableNavigationHandler parent) {
        this.parent = parent;
    }

    @Override
    public NavigationCase getNavigationCase(final FacesContext context, final String fromAction, final String outcome) {
        return parent.getNavigationCase(context, fromAction, outcome);
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
        return parent.getNavigationCases();
    }

    @Override
    public void handleNavigation(final FacesContext context, final String fromAction, final String outcome) {
        BeanManager manager = new BeanManagerLocator().getBeanManager();
        NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();

        NavigationCase navigationCase;
        if (navigationHandler instanceof ConfigurableNavigationHandler) {
            navigationCase = ((ConfigurableNavigationHandler) navigationHandler)
                    .getNavigationCase(context, fromAction, outcome);
        } else {
            navigationCase = getNavigationCase(context, fromAction, outcome);
        }
        manager.fireEvent(new PreNavigateEvent(context, fromAction, outcome, navigationCase));
        parent.handleNavigation(context, fromAction, outcome);
    }

}
