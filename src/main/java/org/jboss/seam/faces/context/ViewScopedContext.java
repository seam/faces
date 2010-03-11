/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jboss.seam.faces.context;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 * This class provides the contexts lifecycle for the new JSF-2 &#064;ViewScoped
 * Context
 * 
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ViewScopedContext implements Context, SystemEventListener
{

    private final static String COMPONENT_MAP_NAME = "org.jboss.seam.faces.viewscope.componentInstanceMap";
    private final static String CREATIONAL_MAP_NAME = "org.jboss.seam.faces.viewscope.creationalInstanceMap";

    private boolean isJsfSubscribed = false;

    @SuppressWarnings("unchecked")
    public <T> T get(final Contextual<T> component)
    {
        assertActive();

        if (!isJsfSubscribed)
        {
            FacesContext.getCurrentInstance().getApplication().subscribeToEvent(PreDestroyViewMapEvent.class, this);

            isJsfSubscribed = true;
        }

        Map<String, Object> viewMap = getViewMap();

        ConcurrentHashMap<Contextual<?>, Object> componentInstanceMap = (ConcurrentHashMap<Contextual<?>, Object>) viewMap
                .get(COMPONENT_MAP_NAME);

        if (componentInstanceMap == null)
        {
            return null;
        }

        T instance = (T) componentInstanceMap.get(component);

        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final Contextual<T> component, final CreationalContext<T> creationalContext)
    {
        assertActive();

        Map<String, Object> viewMap = getViewMap();

        ConcurrentHashMap<Contextual<?>, Object> componentInstanceMap = (ConcurrentHashMap<Contextual<?>, Object>) viewMap
                .get(COMPONENT_MAP_NAME);

        if (componentInstanceMap == null)
        {
            // TODO we now need to start being carefull with reentrancy...
            componentInstanceMap = new ConcurrentHashMap<Contextual<?>, Object>();
            viewMap.put(COMPONENT_MAP_NAME, componentInstanceMap);
        }

        ConcurrentHashMap<Contextual<?>, CreationalContext<?>> creationalContextMap = (ConcurrentHashMap<Contextual<?>, CreationalContext<?>>) viewMap
                .get(CREATIONAL_MAP_NAME);
        if (creationalContextMap == null)
        {
            // TODO we now need to start being carefull with reentrancy...
            creationalContextMap = new ConcurrentHashMap<Contextual<?>, CreationalContext<?>>();
            viewMap.put(CREATIONAL_MAP_NAME, creationalContextMap);
        }

        T instance = (T) componentInstanceMap.get(component);
        if (instance != null)
        {
            return instance;
        }

        if (creationalContext == null)
        {
            return null;
        }

        synchronized (componentInstanceMap)
        {
            // just to make sure...
            T i = (T) componentInstanceMap.get(component);
            if (i != null)
            {
                return i;
            }

            instance = component.create(creationalContext);

            if (instance != null)
            {
                componentInstanceMap.put(component, instance);
                creationalContextMap.put(component, creationalContext);
            }
        }

        return instance;
    }

    public Class<? extends Annotation> getScope()
    {
        return ViewScoped.class;
    }

    /**
     * The view context is active if a valid ViewRoot could be detected.
     */
    public boolean isActive()
    {
        return getViewRoot() != null;
    }

    private void assertActive()
    {
        if (!isActive())
        {
            throw new ContextNotActiveException(
                    "WebBeans context with scope annotation @ViewScoped is not active with respect to the current thread");
        }
    }

    public boolean isListenerForSource(final Object source)
    {
        if (source instanceof UIViewRoot)
        {
            return true;
        }

        return false;
    }

    /**
     * We get PreDestroyViewMapEvent events from the JSF servlet and destroy our
     * contextual instances. This should (theoretically!) also get fired if the
     * webapp closes, so there should be no need to manually track all view
     * scopes and destroy them at a shutdown.
     * 
     * @see javax.faces.event.SystemEventListener#processEvent(javax.faces.event.SystemEvent)
     */
    @SuppressWarnings("unchecked")
    public void processEvent(final SystemEvent event)
    {
        if (event instanceof PreDestroyViewMapEvent)
        {
            // better use the viewmap we get from the event to prevent
            // concurrent modification problems
            Map<String, Object> viewMap = ((UIViewRoot) event.getSource()).getViewMap();

            ConcurrentHashMap<Contextual<?>, Object> componentInstanceMap = (ConcurrentHashMap<Contextual<?>, Object>) viewMap
                    .get(COMPONENT_MAP_NAME);

            ConcurrentHashMap<Contextual<?>, CreationalContext<?>> creationalContextMap = (ConcurrentHashMap<Contextual<?>, CreationalContext<?>>) viewMap
                    .get(CREATIONAL_MAP_NAME);

            if (componentInstanceMap != null)
            {
                for (Entry<Contextual<?>, Object> componentEntry : componentInstanceMap.entrySet())
                {
                    // there is no nice way to explain the Java Compiler that we
                    // are handling the same type T,
                    // therefore we need completely drop the type information :(
                    Contextual contextual = componentEntry.getKey();
                    Object instance = componentEntry.getValue();
                    CreationalContext creational = creationalContextMap.get(contextual);

                    contextual.destroy(instance, creational);
                }
            }
        }
    }

    protected UIViewRoot getViewRoot()
    {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context != null)
        {
            return context.getViewRoot();
        }

        return null;
    }

    protected Map<String, Object> getViewMap()
    {
        UIViewRoot viewRoot = getViewRoot();

        if (viewRoot != null)
        {
            return viewRoot.getViewMap(true);
        }

        return null;
    }
}