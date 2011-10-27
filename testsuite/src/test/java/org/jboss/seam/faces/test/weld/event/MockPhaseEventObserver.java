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
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;

/**
 * @author Nicklas Karlsson
 */
@ApplicationScoped
public class MockPhaseEventObserver {
    private Map<String, List<PhaseId>> observations = new HashMap<String, List<PhaseId>>();

    private void recordObservation(String id, PhaseId observation) {
        List<PhaseId> observed = observations.get(id);
        if (observed == null) {
            observed = new ArrayList<PhaseId>();
            observations.put(id, observed);
        }
        observed.add(observation);
    }

    public void reset() {
        observations.clear();
    }

    public void assertObservations(String id, PhaseId... observations) {
        List<PhaseId> observed = this.observations.get(id);
        assert observed != null && observed.size() == observations.length;
        assert observed.containsAll(Arrays.asList(observations));
    }

    public void observeBeforeRenderResponse(@Observes @Before @RenderResponse final PhaseEvent e) {
        recordObservation("1", e.getPhaseId());
    }

    public void observeAfterRenderResponse(@Observes @After @RenderResponse final PhaseEvent e) {
        recordObservation("2", e.getPhaseId());
    }

    public void observeBeforeApplyRequestValues(@Observes @Before @ApplyRequestValues final PhaseEvent e) {
        recordObservation("3", e.getPhaseId());
    }

    public void observeAfterApplyRequestValues(@Observes @After @ApplyRequestValues final PhaseEvent e) {
        recordObservation("4", e.getPhaseId());
    }

    public void observeBeforeInvokeApplication(@Observes @Before @InvokeApplication final PhaseEvent e) {
        recordObservation("5", e.getPhaseId());
    }

    public void observeAfterInvokeApplication(@Observes @After @InvokeApplication final PhaseEvent e) {
        recordObservation("6", e.getPhaseId());
    }

    public void observeBeforeProcessValidations(@Observes @Before @ProcessValidations final PhaseEvent e) {
        recordObservation("7", e.getPhaseId());
    }

    public void observeAfterProcessValidations(@Observes @After @ProcessValidations final PhaseEvent e) {
        recordObservation("8", e.getPhaseId());
    }

    public void observeBeforeRestoreView(@Observes @Before @RestoreView final PhaseEvent e) {
        recordObservation("9", e.getPhaseId());
    }

    public void observeAfterRestoreView(@Observes @After @RestoreView final PhaseEvent e) {
        recordObservation("10", e.getPhaseId());
    }

    public void observeBeforeUpdateModelValues(@Observes @Before @UpdateModelValues final PhaseEvent e) {
        recordObservation("11", e.getPhaseId());
    }

    public void observeAfterUpdateModelValues(@Observes @After @UpdateModelValues final PhaseEvent e) {
        recordObservation("12", e.getPhaseId());
    }

    public void observeAllRenderResponse(@Observes @RenderResponse final PhaseEvent e) {
        recordObservation("13", e.getPhaseId());
    }

    public void observeAllApplyRequestValues(@Observes @ApplyRequestValues final PhaseEvent e) {
        recordObservation("14", e.getPhaseId());
    }

    public void observeAllInvokeApplication(@Observes @InvokeApplication final PhaseEvent e) {
        recordObservation("15", e.getPhaseId());
    }

    public void observeAllProcessValidations(@Observes @ProcessValidations final PhaseEvent e) {
        recordObservation("16", e.getPhaseId());
    }

    public void observeAllRestoreView(@Observes @RestoreView final PhaseEvent e) {
        recordObservation("17", e.getPhaseId());
    }

    public void observeAllUpdateModelValues(@Observes @UpdateModelValues final PhaseEvent e) {
        recordObservation("18", e.getPhaseId());
    }

    public void observeAllBeforeEvents(@Observes @Before final PhaseEvent e) {
        recordObservation("19", e.getPhaseId());
    }

    public void observeAllAfterEvents(@Observes @Before final PhaseEvent e) {
        recordObservation("20", e.getPhaseId());
    }

    public void observeAllEvents(@Observes final PhaseEvent e) {
        recordObservation("21", e.getPhaseId());
    }

}
