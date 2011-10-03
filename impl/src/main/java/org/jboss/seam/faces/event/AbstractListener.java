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

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.solder.beanManager.BeanManagerAware;

/**
 * Superclass for event listeners
 *
 * @param <T> Listener class
 * @author Nicklas Karlsson
 */
public class AbstractListener<T extends EventListener> extends BeanManagerAware {
    @SuppressWarnings("unchecked")
    protected List<T> getListeners(Class<? extends T>... classes) {
        List<T> listeners = new ArrayList<T>();
        for (Class<? extends T> clazz : classes) {
            Bean<? extends T> bean = (Bean<? extends T>) getBeanManager().getBeans(clazz).iterator().next();
            CreationalContext<? extends T> context = getBeanManager().createCreationalContext(bean);
            T listener = (T) getBeanManager().getReference(bean, clazz, context);
            listeners.add(listener);
        }
        return listeners;
    }

    /**
     * Create contextual instances for the specified listener classes, excluding any listeners that do not correspond to an
     * enabled bean.
     */
    @SuppressWarnings("unchecked")
    protected List<T> getEnabledListeners(Class<? extends T>... classes) {
        List<T> listeners = new ArrayList<T>();
        for (Class<? extends T> clazz : classes) {
            Set<Bean<?>> beans = getBeanManager().getBeans(clazz);
            if (!beans.isEmpty()) {
                Bean<T> bean = (Bean<T>) getBeanManager().resolve(beans);
                CreationalContext<T> context = getBeanManager().createCreationalContext(bean);
                listeners.add((T) getBeanManager().getReference(bean, clazz, context));
            }
        }
        return listeners;
    }

}
