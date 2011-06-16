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
package org.jboss.seam.faces.component;

import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.component.ActionSource2;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.view.ViewMetadata;
import javax.faces.webapp.FacesServlet;

/**
 * <p>
 * <strong>UIViewAction</strong> is an {@link ActionSource2} {@link UIComponent} that specifies an application-specific command
 * (or action)--defined as an EL method expression--to be invoked during one of the JSF lifecycle phases that proceeds view
 * rendering. This component must be declared as a child of the {@link ViewMetadata} facet of the {@link UIViewRoot} so that it
 * gets incorporated into the JSF lifecycle on both non-faces (initial) requests and faces (postback) requests.
 * </p>
 * <p/>
 * <p>
 * The purpose of this component is to provide a light-weight front-controller solution for executing code upon the loading of a
 * JSF view to support the integration of system services, content retrieval, view management, and navigation. This
 * functionality is especially useful for non-faces (initial) requests.
 * </p>
 * <p/>
 * <p>
 * The {@link UIViewAction} component is closely tied to the {@link UIViewParameter} component. The {@link UIViewParameter}
 * component binds a request parameter to a model property. Most of the time, this binding is used to populate the model with
 * data that supports the method being invoked by a {@link UIViewAction} component, much like form inputs populate the model
 * with data to support the method being invoked by a {@link UICommand} component.
 * </p>
 * <p/>
 * <p>
 * When the <literal>decode()</literal> method of the {@link UIViewAction} is invoked, it will queue an {@link ActionEvent} to
 * be broadcast to all interested listeners when the <literal>broadcast()</literal> method is invoked.
 * </p>
 * <p/>
 * <p>
 * If the value of the component's <literal>immediate</literal> attribute is <literal>true</literal>, the action will be invoked
 * during the Apply Request Values JSF lifecycle phase. Otherwise, the action will be invoked during the Invoke Application
 * phase, the default behavior. The phase can be set explicitly in the <literal>phase</literal> attribute, which takes
 * precedence over the <literal>immediate</literal> attribute.
 * </p>
 * <p/>
 * <p>
 * The invocation of the action is normally suppressed (meaning the {@link ActionEvent} is not queued) on a faces request. It
 * can be enabled by setting the component's <literal>onPostback</literal> attribute to <literal>true</literal>. Execution of
 * the method can be subject to a required condition for all requests by assigning an EL value expression of expected type
 * boolean to the component's <literal>if</literal> attribute, which must evaluate to <literal>true</literal> for the action to
 * be invoked.
 * </p>
 * <p/>
 * <p>
 * The {@link NavigationHandler} is consulted after the action is invoked to carry out the navigation case that matches the
 * action signature and outcome. If a navigation case is matched, or the response is marked complete by the action, subsequent
 * {@link UIViewAction} components associated with the current view are short-circuited. The lifecycle then advances
 * appropriately.
 * </p>
 * <p/>
 * <p>
 * It's important to note that the full component tree is not built before the UIViewAction components are processed on an
 * non-faces (initial) request. Rather, the component tree only contains the {@link ViewMetadata}, an important part of the
 * optimization of this component and what sets it apart from a {@link PreRenderViewEvent} listener.
 * </p>
 *
 * @author Dan Allen
 * @author Andy Schwartz
 * @see UIViewParameter
 */
@FacesComponent(
// tagName = "viewAction",
// namespace = "http://jboss.org/seam/faces",
// (see
// https://javaserverfaces-spec-public.dev.java.net/issues/show_bug.cgi?id=594)
        value = UIViewAction.COMPONENT_TYPE)
public class UIViewAction extends UIComponentBase implements ActionSource2 {

    // ------------------------------------------------------ Manifest Constants

    /**
     * <p>
     * The standard component type for this component.
     * </p>
     */
    public static final String COMPONENT_TYPE = "org.jboss.seam.faces.ViewAction";

    /**
     * <p>
     * The standard component family for this component.
     * </p>
     */
    public static final String COMPONENT_FAMILY = "org.jboss.seam.faces.ViewAction";

    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {

        onPostback, actionExpression, immediate, phase, ifAttr("if");
        private String name;

        PropertyKeys() {
        }

        PropertyKeys(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name != null ? name : super.toString();
        }
    }

    // ------------------------------------------------------------ Constructors

    /**
     * <p>
     * Create a new {@link UIViewAction} instance with default property values.
     * </p>
     */
    public UIViewAction() {
        super();
        setRendererType(null);
    }

    // -------------------------------------------------------------- Properties
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated This has been replaced by {@link #getActionExpression}.
     */
    @Deprecated
    public MethodBinding getAction() {
        MethodBinding result = null;
        MethodExpression me;

        if (null != (me = getActionExpression())) {
            result = new MethodBindingMethodExpressionAdapter(me);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException if called
     * @deprecated This has been replaced by {@link #setActionExpression(javax.el.MethodExpression)}.
     */
    @Deprecated
    public void setAction(final MethodBinding action) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Action listeners are not supported by the {@link UIViewAction} component.
     *
     * @throws UnsupportedOperationException if called
     */
    @SuppressWarnings("deprecation")
    public MethodBinding getActionListener() {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Action listeners are not supported by the {@link UIViewAction} component.
     *
     * @throws UnsupportedOperationException if called
     */
    @SuppressWarnings("deprecation")
    public void setActionListener(final MethodBinding actionListener) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Returns the value which dictates the JSF lifecycle phase in which the action is invoked. If the value of this attribute
     * is <literal>true</literal>, the action will be invoked in the Apply Request Values phase. If the value of this attribute
     * is <literal>false</literal>, the default, the action will be invoked in the Invoke Application Phase.
     */
    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    /**
     * Sets the immediate flag, which controls the JSF lifecycle in which the action is invoked.
     */
    public void setImmediate(final boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    /**
     * <p>
     * Returns the name of the phase in which the action is to be queued. Only the following phases are supported (case does not
     * matter):
     * </p>
     * <ul>
     * <li>APPLY_REQUEST_VALUES</li>
     * <li>PROCESS_VALIDATIONS</li>
     * <li>UPDATE_MODEL_VALUES</li>
     * <li>INVOKE_APPLICATION</li>
     * </ul>
     * <p>
     * If the phase is set, it takes precedence over the immediate flag.
     * </p>
     */
    public String getPhase() {
        String phase = (String) getStateHelper().eval(PropertyKeys.phase);
        if (phase != null) {
            phase = phase.toUpperCase();
        }
        return phase;
    }

    /**
     * Set the name of the phase in which the action is to be queued.
     */
    public void setPhase(final String phase) {
        getStateHelper().put(PropertyKeys.phase, phase);
    }

    public PhaseId getPhaseId() {
        String phase = getPhase();
        if (phase == null) {
            return null;
        }
        if ("APPLY_REQUEST_VALUES".equals(phase)) {
            return PhaseId.APPLY_REQUEST_VALUES;
        } else if ("PROCESS_VALIDATIONS".equals(phase)) {
            return PhaseId.PROCESS_VALIDATIONS;
        } else if ("UPDATE_MODEL_VALUES".equals(phase)) {
            return PhaseId.UPDATE_MODEL_VALUES;
        } else if ("INVOKE_APPLICATION".equals(phase)) {
            return PhaseId.INVOKE_APPLICATION;
        } else if ("ANY_PHASE".equals(phase) || "RESTORE_VIEW".equals(phase) || "RENDER_RESPONSE".equals(phase)) {
            throw new FacesException("View actions cannot be executed in specified phase: [" + phase + "]");
        } else {
            throw new FacesException("Not a valid phase [" + phase + "]");
        }
    }

    /**
     * Action listeners are not supported by the {@link UIViewAction} component.
     *
     * @throws UnsupportedOperationException if called
     */
    public void addActionListener(final ActionListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Action listeners are not supported by the {@link UIViewAction} component.
     */
    public ActionListener[] getActionListeners() {
        return new ActionListener[0];
    }

    /**
     * Action listeners are not supported by the {@link UIViewAction} component.
     *
     * @throws UnsupportedOperationException if called
     */
    public void removeActionListener(final ActionListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Returns the action, represented as an EL method expression, to invoke.
     */
    public MethodExpression getActionExpression() {
        return (MethodExpression) getStateHelper().get(PropertyKeys.actionExpression);
    }

    /**
     * Sets the action, represented as an EL method expression, to invoke.
     */
    public void setActionExpression(final MethodExpression actionExpression) {
        getStateHelper().put(PropertyKeys.actionExpression, actionExpression);
    }

    /**
     * Returns a boolean value that controls whether the action is invoked during faces (postback) request. The default is
     * false.
     */
    public boolean isOnPostback() {
        return (Boolean) getStateHelper().eval(PropertyKeys.onPostback, false);
    }

    /**
     * Set the bookean flag that controls whether the action is invoked during a faces (postback) request.
     */
    public void setOnPostback(final boolean onPostback) {
        getStateHelper().put(PropertyKeys.onPostback, onPostback);
    }

    /**
     * Returns a condition, represented as an EL value expression, that must evaluate to true for the action to be invoked.
     */
    public boolean isIf() {
        return (Boolean) getStateHelper().eval(PropertyKeys.ifAttr, true);
    }

    /**
     * Sets the condition, represented as an EL value expression, that must evaluate to true for the action to be invoked.
     */
    public void setIf(final boolean condition) {
        getStateHelper().put(PropertyKeys.ifAttr, condition);
    }

    // ----------------------------------------------------- UIComponent Methods

    /**
     * <p>
     * In addition to to the default {@link UIComponent#broadcast} processing, pass the {@link ActionEvent} being broadcast to
     * the default {@link ActionListener} registered on the {@link javax.faces.application.Application}.
     * </p>
     *
     * @param event {@link FacesEvent} to be broadcast
     * @throws AbortProcessingException Signal the JavaServer Faces implementation that no further processing on the current
     *                                  event should be performed
     * @throws IllegalArgumentException if the implementation class of this {@link FacesEvent} is not supported by this
     *                                  component
     * @throws NullPointerException     if <code>event</code> is <code>null</code>
     */
    @Override
    public void broadcast(final FacesEvent event) throws AbortProcessingException {

        super.broadcast(event);

        FacesContext context = getFacesContext();

        // OPEN QUESTION: should we consider a navigation to the same view as a
        // no-op navigation?

        // only proceed if the response has not been marked complete and
        // navigation to another view has not occurred
        if ((event instanceof ActionEvent) && !context.getResponseComplete() && (context.getViewRoot() == getViewRootOf(event))) {
            ActionListener listener = context.getApplication().getActionListener();
            if (listener != null) {
                UIViewRoot viewRootBefore = context.getViewRoot();
                InstrumentedFacesContext instrumentedContext = new InstrumentedFacesContext(context);
                // defer the call to renderResponse() that happens in
                // ActionListener#processAction(ActionEvent)
                instrumentedContext.disableRenderResponseControl().set();
                listener.processAction((ActionEvent) event);
                instrumentedContext.restore();
                // if the response is marked complete, the story is over
                if (!context.getResponseComplete()) {
                    UIViewRoot viewRootAfter = context.getViewRoot();
                    // if the view id changed as a result of navigation, then execute
                    // the JSF lifecycle for the new view id
                    if (viewRootBefore != viewRootAfter) {
                        /*
                         * // execute the JSF lifecycle by dispatching a forward request // this approach is problematic because
                         * it throws a wrench in the event broadcasting try { context.getExternalContext
                         * ().dispatch(context.getApplication() .getViewHandler().getActionURL(context,
                         * viewRootAfter.getViewId()) .substring(context.getExternalContext
                         * ().getRequestContextPath().length())); // kill this lifecycle execution context.responseComplete(); }
                         * catch (IOException e) { throw new FacesException("Dispatch to viewId failed: " +
                         * viewRootAfter.getViewId(), e); }
                         */

                        // manually execute the JSF lifecycle on the new view id
                        // certain tweaks have to be made to the FacesContext to allow
                        // us to reset the lifecycle
                        Lifecycle lifecycle = getLifecycle(context);
                        instrumentedContext = new InstrumentedFacesContext(context);
                        instrumentedContext.pushViewIntoRequestMap().clearViewRoot().clearPostback().set();
                        lifecycle.execute(instrumentedContext);
                        instrumentedContext.restore();

                        /*
                         * Another approach would be to register a phase listener in the decode() method for the phase in which
                         * the action is set to invoke. The phase listener would performs a servlet forward if a non-redirect
                         * navigation occurs after the phase.
                         */
                    } else {
                        // apply the deferred call (relevant when immediate is true)
                        context.renderResponse();
                    }
                }
            }
        }
    }

    /**
     * First, determine if the action should be invoked by evaluating this components pre-conditions. If this is a faces
     * (postback) request and the evaluated value of the postback attribute is false, take no action. If the evaluated value of
     * the if attribute is false, take no action. If both conditions pass, proceed with creating an {@link ActionEvent}.
     * <p/>
     * Set the phaseId in which the queued {@link ActionEvent} should be broadcast by assigning the appropriate value to the
     * phaseId property of the {@link ActionEvent} according to the evaluated value of the immediate attribute. If the value is
     * <literal>true</literal>, set the phaseId to {@link PhaseId#APPLY_REQUEST_VALUES}. Otherwise, set the phaseId to to
     * {@link PhaseId#INVOKE_APPLICATION}.
     * <p/>
     * Finally, queue the event by calling <literal>queueEvent()</literal> and passing the {@link ActionEvent} just created.
     */
    @Override
    public void decode(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        if ((context.isPostback() && !isOnPostback()) || !isIf()) {
            return;
        }

        ActionEvent e = new ActionEvent(this);
        PhaseId phaseId = getPhaseId();
        if (phaseId != null) {
            e.setPhaseId(phaseId);
        } else if (isImmediate()) {
            e.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            e.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }

        queueEvent(e);
    }

    private UIViewRoot getViewRootOf(final FacesEvent e) {
        UIComponent c = e.getComponent();
        do {
            if (c instanceof UIViewRoot) {
                return (UIViewRoot) c;
            }
            c = c.getParent();
        } while (c != null);
        return null;
    }

    private Lifecycle getLifecycle(final FacesContext context) {
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        String lifecycleId = context.getExternalContext().getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);
        if (lifecycleId == null) {
            lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
        }
        return lifecycleFactory.getLifecycle(lifecycleId);
    }

    /**
     * A FacesContext delegator that gives us the necessary controls over the FacesContext to allow the execution of the
     * lifecycle to accomodate the UIViewAction sequence.
     */
    private class InstrumentedFacesContext extends FacesContextWrapper {

        private final FacesContext wrapped;
        private boolean viewRootCleared = false;
        private boolean renderedResponseControlDisabled = false;
        private Boolean postback = null;

        public InstrumentedFacesContext(final FacesContext wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public FacesContext getWrapped() {
            return wrapped;
        }

        @Override
        public UIViewRoot getViewRoot() {
            if (viewRootCleared) {
                return null;
            }

            return wrapped.getViewRoot();
        }

        @Override
        public void setViewRoot(final UIViewRoot viewRoot) {
            viewRootCleared = false;
            wrapped.setViewRoot(viewRoot);
        }

        @Override
        public boolean isPostback() {
            return postback == null ? wrapped.isPostback() : postback;
        }

        @Override
        public void renderResponse() {
            if (!renderedResponseControlDisabled) {
                wrapped.renderResponse();
            }
        }

        /**
         * Make it look like we have dispatched a request using the include method.
         */
        public InstrumentedFacesContext pushViewIntoRequestMap() {
            getExternalContext().getRequestMap().put("javax.servlet.include.servlet_path", wrapped.getViewRoot().getViewId());
            return this;
        }

        public InstrumentedFacesContext clearPostback() {
            postback = false;
            return this;
        }

        public InstrumentedFacesContext clearViewRoot() {
            viewRootCleared = true;
            return this;
        }

        public InstrumentedFacesContext disableRenderResponseControl() {
            renderedResponseControlDisabled = true;
            return this;
        }

        public void set() {
            setCurrentInstance(this);
        }

        public void restore() {
            setCurrentInstance(wrapped);
        }
    }
}
