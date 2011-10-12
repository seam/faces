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
package org.jboss.seam.faces.test.weld.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PostRestoreStateEvent;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.PreRemoveFromViewEvent;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.event.SystemEvent;

import org.jboss.seam.faces.event.qualifier.Component;
import org.jboss.seam.faces.event.qualifier.View;

/**
 * @author Nicklas Karlsson
 */
@ApplicationScoped
public class SystemEventObserver {
    private Map<String, List<SystemEvent>> observations = new HashMap<String, List<SystemEvent>>();

    private void recordObservation(String id, SystemEvent observation) {
        List<SystemEvent> observed = observations.get(id);
        if (observed == null) {
            observed = new ArrayList<SystemEvent>();
            observations.put(id, observed);
        }
        observed.add(observation);
    }

    public void reset() {
        observations.clear();
    }

    public void assertObservations(String id, SystemEvent... observations) {
        List<SystemEvent> observed = this.observations.get(id);
        assert observed != null && observed.size() == observations.length;
        assert observed.containsAll(Arrays.asList(observations));
    }

    public void observe(@Observes PostConstructApplicationEvent e) {
        recordObservation("1", e);
    }

    public void observe(@Observes PreDestroyApplicationEvent e) {
        recordObservation("2", e);
    }

    public void observe(@Observes PostConstructCustomScopeEvent e) {
        recordObservation("3", e);
    }

    public void observe(@Observes PreDestroyCustomScopeEvent e) {
        recordObservation("4", e);
    }

    public void observe(@Observes ExceptionQueuedEvent e) {
        recordObservation("5", e);
    }

    public void observe(@Observes ComponentSystemEvent e) {
        recordObservation("6", e);
    }

    public void observe(@Observes PreValidateEvent e) {
        recordObservation("7", e);
    }

    public void observe2(@Observes @Component("foo") PreValidateEvent e) {
        recordObservation("8", e);
    }

    public void observe3(@Observes @Component("foo") ComponentSystemEvent e) {
        recordObservation("9", e);
    }

    public void observe(@Observes PostValidateEvent e) {
        recordObservation("10", e);
    }

    public void observe2(@Observes @Component("foo") PostValidateEvent e) {
        recordObservation("11", e);
    }

    public void observe(@Observes PostAddToViewEvent e) {
        recordObservation("12", e);
    }

    public void observe2(@Observes @Component("foo") PostAddToViewEvent e) {
        recordObservation("13", e);
    }

    public void observe(@Observes PostConstructViewMapEvent e) {
        recordObservation("14", e);
    }

    public void observe2(@Observes @View("foo.xhtml") PostConstructViewMapEvent e) {
        recordObservation("14a", e);
    }

    public void observe(@Observes PostRestoreStateEvent e) {
        recordObservation("15", e);
    }

    public void observe2(@Observes @Component("foo") PostRestoreStateEvent e) {
        recordObservation("16", e);
    }

    public void observe(@Observes PreDestroyViewMapEvent e) {
        recordObservation("17", e);
    }

    public void observe2(@Observes @View("foo.xhtml") PreDestroyViewMapEvent e) {
        recordObservation("17a", e);
    }

    public void observe(@Observes PreRemoveFromViewEvent e) {
        recordObservation("18", e);
    }

    public void observe2(@Observes @Component("foo") PreRemoveFromViewEvent e) {
        recordObservation("19", e);
    }

    public void observe(@Observes PreRenderComponentEvent e) {
        recordObservation("20", e);
    }

    public void observe2(@Observes @Component("foo") PreRenderComponentEvent e) {
        recordObservation("21", e);
    }

    public void observe(@Observes PreRenderViewEvent e) {
        recordObservation("22", e);
    }

    public void observe2(@Observes @View("foo.xhtml") PreRenderViewEvent e) {
        recordObservation("23", e);
    }
}
