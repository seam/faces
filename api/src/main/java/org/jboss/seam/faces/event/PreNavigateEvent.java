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

import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

/**
 * An event that is fired when JSF navigation is invoked via the {@link NavigationHandler}, but before any redirecting or
 * non-redirecting navigation is completed, giving consumers of this event a chance to take action.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
public class PreNavigateEvent {

    private final FacesContext context;
    private final String fromAction;
    private final String outcome;
    private final NavigationCase navigationCase;

    public PreNavigateEvent(final FacesContext context, final String fromAction, final String outcome,
                            final NavigationCase navigationCase) {
        this.context = context;
        this.fromAction = fromAction;
        this.outcome = outcome;
        this.navigationCase = navigationCase;
    }

    public FacesContext getContext() {
        return context;
    }

    public String getFromAction() {
        return fromAction;
    }

    public String getOutcome() {
        return outcome;
    }

    public NavigationCase getNavigationCase() {
        return navigationCase;
    }

}
