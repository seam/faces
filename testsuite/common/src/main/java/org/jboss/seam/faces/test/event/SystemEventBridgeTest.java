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

import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
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
import javax.faces.event.ScopeContext;
import javax.faces.event.SystemEvent;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.event.SystemEventBridge;
import org.jboss.solder.beanManager.BeanManagerAware;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.faces.mock.application.MockApplication;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nicklas Karlsson
 */
@RunWith(Arquillian.class)
public class SystemEventBridgeTest {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(SystemEventObserver.class, SystemEventBridge.class, BeanManagerAware.class)
                .addAsManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
    }

    @Inject
    SystemEventBridge listener;

    @Inject
    SystemEventObserver observer;

    private final MockFacesContext facesContext = new MockFacesContext();
    private final MockApplication application = new MockApplication();
    private final ScopeContext scopeContext = new ScopeContext("foo", new HashMap<String, Object>());
    private final ExceptionQueuedEventContext eventContext = new ExceptionQueuedEventContext(facesContext,
            new NullPointerException());
    private static final UIComponent component = new UIOutput();
    private static final UIViewRoot uiViewRoot = new UIViewRoot();

    static {
        component.setId("foo");
        uiViewRoot.setViewId("foo.xhtml");
    }

    @Test
    public void testObservePostConstructApplication() {
        fireAndAssert("1", new PostConstructApplicationEvent(application));
    }

    @Test
    public void testObservePreDestroyApplication() {
        fireAndAssert("2", new PreDestroyApplicationEvent(application));
    }

    @Test
    public void testObservePostConstructCustomScope() {
        fireAndAssert("3", new PostConstructCustomScopeEvent(scopeContext));
    }

    @Test
    public void testObservePreDestroyCustomScope() {
        fireAndAssert("4", new PreDestroyCustomScopeEvent(scopeContext));
    }

    @Test
    public void testObserveExceptionQueued() {
        fireAndAssert("5", new ExceptionQueuedEvent(eventContext));
    }

    @Test
    public void testObserveComponentSystemEvent() {
        fireAndAssert("6", new PreValidateEvent(component));
    }

    @Test
    public void testObservePreValidate() {
        fireAndAssert("7", new PreValidateEvent(component));
    }

    @Test
    public void testObservePreValidateComponent() {
        fireAndAssert("8", new PreValidateEvent(component));
    }

    @Test
    public void testObserveComponent() {
        fireAndAssert("9", new PreValidateEvent(component));
    }

    @Test
    public void testObservePostValidate() {
        fireAndAssert("10", new PostValidateEvent(component));
    }

    @Test
    public void testObservePostValidateComponent() {
        fireAndAssert("11", new PostValidateEvent(component));
    }

    @Test
    public void testObservePostAddToView() {
        fireAndAssert("12", new PostAddToViewEvent(component));
    }

    @Test
    public void testObservePostAddToViewComponent() {
        fireAndAssert("13", new PostAddToViewEvent(component));
    }

    @Test
    public void testObservePostConstructViewMap() {
        fireAndAssert("14", new PostConstructViewMapEvent(uiViewRoot));
    }

    @Test
    public void testObservePostConstructSpecificViewMap() {
        fireAndAssert("14a", new PostConstructViewMapEvent(uiViewRoot));
    }

    @Test
    public void testObservePostRestoreState() {
        fireAndAssert("15", new PostRestoreStateEvent(component));
    }

    @Test
    public void testObservePostRestoreStateComponent() {
        fireAndAssert("16", new PostRestoreStateEvent(component));
    }

    @Test
    public void testObservePreDestroyViewMap() {
        fireAndAssert("17", new PreDestroyViewMapEvent(uiViewRoot));
    }

    @Test
    public void testObservePreDestroySpecificViewMap() {
        fireAndAssert("17a", new PreDestroyViewMapEvent(uiViewRoot));
    }

    @Test
    public void testObservePreRemoveFromView() {
        fireAndAssert("18", new PreRemoveFromViewEvent(component));
    }

    @Test
    public void testObservePreRemoveFromViewComponent() {
        fireAndAssert("19", new PreRemoveFromViewEvent(component));
    }

    @Test
    public void testObservePreRenderComponent() {
        fireAndAssert("20", new PreRenderComponentEvent(component));
    }

    @Test
    public void testObservePreRenderComponentComponent() {
        fireAndAssert("21", new PreRenderComponentEvent(component));
    }

    @Test
    public void testObservePreRenderView() {
        fireAndAssert("22", new PreRenderViewEvent(uiViewRoot));
    }

    @Test
    public void testObservePreRenderSpecificView() {
        fireAndAssert("23", new PreRenderViewEvent(uiViewRoot));
    }

    private void fireAndAssert(final String caseId, final SystemEvent... events) {
        observer.reset();
        for (SystemEvent e : events) {
            listener.processEvent(e);
        }
        observer.assertObservations(caseId, events);
    }

    /*
     * @Test public void testSpecificPostComponentValidation() { UIComponent c = new UIOutput(); c.setId("foo");
     * systemEventListener.processEvent(new PostValidateEvent(c)); assert SystemEventObserver.specificComponentValidationEvent;
     * }
     *
     * @Test public void testExceptionQueuedEventObserver() { ExceptionQueuedEventContext eqec = new
     * ExceptionQueuedEventContext(facesContext, new NullPointerException()); ExceptionQueuedEvent eqe = new
     * ExceptionQueuedEvent(eqec); systemEventListener.processEvent(eqe); assert SystemEventObserver.excecptionQueuedEvent; }
     *
     * @Test public void testPostConstructApplicationEventObserver() { systemEventListener.processEvent(new
     * PostConstructApplicationEvent(application)); assert SystemEventObserver.postConstructApplicationEvent; }
     *
     * @Test public void testPostConstructCustomScopeEvent() { ScopeContext sc = new ScopeContext("dummyscope", new
     * HashMap<String, Object>()); systemEventListener.processEvent(new PostConstructCustomScopeEvent(sc)); assert
     * SystemEventObserver.postConstructCustomScopeEvent; }
     *
     * @Test public void testPreDestroyApplicationEventObserver() { systemEventListener.processEvent(new
     * PreDestroyApplicationEvent(application)); assert SystemEventObserver.preDestroyApplicationEvent; }
     *
     * @Test public void testPreDestroyCustomScopeEventObserver() { ScopeContext sc = new ScopeContext("dummyscope", new
     * HashMap<String, Object>()); systemEventListener.processEvent(new PreDestroyCustomScopeEvent(sc)); assert
     * SystemEventObserver.preDestroyCustomScopeEvent; }
     */
}
