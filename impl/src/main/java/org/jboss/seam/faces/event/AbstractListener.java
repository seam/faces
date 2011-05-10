package org.jboss.seam.faces.event;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.seam.solder.beanManager.BeanManagerAware;

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
