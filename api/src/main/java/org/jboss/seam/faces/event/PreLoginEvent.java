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

import javax.faces.context.FacesContext;

/**
 * An event fired when the {@link SecurityPhaseListener} is about to redirect to the login page.  The session map is
 * provided as a place to store values to be retrieved during the {@link PostLoginEvent}.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class PreLoginEvent {
    private final FacesContext context;
    private final Map<String, Object> sessionMap;

    public PreLoginEvent(FacesContext context, Map<String, Object> sessionMap) {
        this.context = context;
        this.sessionMap = sessionMap;
    }

    public FacesContext getFacesContext() {
        return context;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }
}
