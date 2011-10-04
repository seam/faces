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
package org.jboss.seam.faces.test.event;

import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.event.PhaseEventBridge;
import org.jboss.seam.faces.test.MockLogger;
import org.jboss.seam.faces.test.MockLoggerProducer;
import org.jboss.seam.faces.test.PhaseTestBase;
import org.jboss.solder.beanManager.BeanManagerAware;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nicklas Karlsson
 */
@RunWith(Arquillian.class)
public class PhaseEventBridgeTest extends PhaseTestBase {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(MockPhaseEventObserver.class, PhaseEventBridge.class, BeanManagerAware.class, MockLogger.class, MockLoggerProducer.class)
                .addAsManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
    }

    @Inject
    MockPhaseEventObserver observer;

    @Test
    public void testBeforeRenderResponseObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("1", PhaseId.RENDER_RESPONSE);
    }

    @Test
    public void testAfterRenderResponseObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("2", PhaseId.RENDER_RESPONSE);
    }

    @Test
    public void testBeforeApplyRequestValuesObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("3", PhaseId.APPLY_REQUEST_VALUES);
    }

    @Test
    public void testAfterApplyRequestValuesObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("4", PhaseId.APPLY_REQUEST_VALUES);
    }

    @Test
    public void testBeforeInvokeApplicationObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("5", PhaseId.INVOKE_APPLICATION);
    }

    @Test
    public void testAfterInvokeApplicationObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("6", PhaseId.INVOKE_APPLICATION);
    }

    @Test
    public void testBeforeProcessValidationsObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("7", PhaseId.PROCESS_VALIDATIONS);
    }

    @Test
    public void testAfterProcessValidationsObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("8", PhaseId.PROCESS_VALIDATIONS);
    }

    @Test
    public void testBeforeRestoreViewObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("9", PhaseId.RESTORE_VIEW);
    }

    @Test
    public void testAfterRestoreViewObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("10", PhaseId.RESTORE_VIEW);
    }

    @Test
    public void testBeforeUpdateModelValuesObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("11", PhaseId.UPDATE_MODEL_VALUES);
    }

    @Test
    public void testAfterUpdateModelValuesObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("11", PhaseId.UPDATE_MODEL_VALUES);
    }

    @Test
    public void testAllRenderResponseObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("13", PhaseId.RENDER_RESPONSE, PhaseId.RENDER_RESPONSE);
    }

    @Test
    public void testAllApplyRequestValuesObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("14", PhaseId.APPLY_REQUEST_VALUES, PhaseId.APPLY_REQUEST_VALUES);
    }

    @Test
    public void testAllInvokeApplicationObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("15", PhaseId.INVOKE_APPLICATION, PhaseId.INVOKE_APPLICATION);
    }

    @Test
    public void testAllProcessValidationsObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("16", PhaseId.PROCESS_VALIDATIONS, PhaseId.PROCESS_VALIDATIONS);
    }

    @Test
    public void testAllRestoreViewObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("17", PhaseId.RESTORE_VIEW, PhaseId.RESTORE_VIEW);
    }

    @Test
    public void testAllUpdateModelValuesObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("18", PhaseId.UPDATE_MODEL_VALUES, PhaseId.UPDATE_MODEL_VALUES);
    }

    @Test
    public void testAllBeforeEventsObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("19", PhaseId.APPLY_REQUEST_VALUES, PhaseId.INVOKE_APPLICATION,
                PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE, PhaseId.RESTORE_VIEW, PhaseId.UPDATE_MODEL_VALUES);
    }

    @Test
    public void testAllAfterEventsObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("20", PhaseId.APPLY_REQUEST_VALUES, PhaseId.INVOKE_APPLICATION,
                PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE, PhaseId.RESTORE_VIEW, PhaseId.UPDATE_MODEL_VALUES);
    }

    @Test
    public void testAllEventsObserver() {
        observer.reset();
        fireAllPhases();
        observer.assertObservations("21", PhaseId.APPLY_REQUEST_VALUES, PhaseId.INVOKE_APPLICATION,
                PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE, PhaseId.RESTORE_VIEW, PhaseId.UPDATE_MODEL_VALUES,
                PhaseId.APPLY_REQUEST_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE,
                PhaseId.RESTORE_VIEW, PhaseId.UPDATE_MODEL_VALUES);
    }

}
