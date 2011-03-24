package org.jboss.seam.faces.util;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import org.jboss.seam.faces.component.FormValidationTypeOverrideExtension;

/**
 * A utility providing common functions to simply use of {@link BeanManager}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class BeanManagerUtils {
    @Inject
    private BeanManager manager;

    @Inject
    private FormValidationTypeOverrideExtension classExtension;

    /**
     * Perform @{@link Inject} on an object as if it were a bean managed by CDI.
     * 
     * @param instance
     */
    @SuppressWarnings("unchecked")
    public void injectNonContextualInstance(final Object instance) {
        if (instance != null) {
            CreationalContext<Object> creationalContext = manager.createCreationalContext(null);
            InjectionTarget<Object> injectionTarget = (InjectionTarget<Object>) manager
                    .createInjectionTarget(getAnnotatedType(instance));
            injectionTarget.inject(instance, creationalContext);
        }
    }

    private AnnotatedType<? extends Object> getAnnotatedType(final Object instance) {
        AnnotatedType<?> result = null;
        if (classExtension.hasOverriddenType(instance.getClass())) {
            result = classExtension.getOverriddenType(instance.getClass());
        } else {
            result = manager.createAnnotatedType(instance.getClass());
        }
        return result;
    }

    /**
     * Determine if a bean is {@link Dependent} scoped.
     */
    @SuppressWarnings("unchecked")
    public <T> boolean isDependentScoped(final Class<T> type) {
        Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
        if (bean != null) {
            return Dependent.class.equals(bean.getScope());
        }
        return false;
    }

    /**
     * Get a single CDI managed instance of a specific class. Return only the first result if multiple beans are available.
     * 
     * @param type The class for which to return an instance.
     * @return The managed instance, or null if none could be provided.
     */
    public <T> T getContextualInstance(final Class<T> type) {
        return getContextualInstance(manager, type);
    }

    /**
     * Get a single CDI managed instance of a specific class. Return only the first result if multiple beans are available.
     * <p>
     * <b>NOTE:</b> Using this method should be avoided at all costs.
     * 
     * @param manager The bean manager with which to perform the lookup.
     * @param type The class for which to return an instance.
     * @return The managed instance, or null if none could be provided.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getContextualInstance(final BeanManager manager, final Class<T> type) {
        T result = null;
        Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
        if (bean != null) {
            CreationalContext<T> context = manager.createCreationalContext(bean);
            if (context != null) {
                result = (T) manager.getReference(bean, type, context);
            }
        }
        return result;
    }

    /**
     * Get all CDI managed instances of a specific class. Return results in a {@link List} in no specific order.
     * 
     * @param type The class for which to return instances.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getContextualInstances(final Class<T> type) {
        List<T> result = new ArrayList<T>();
        for (Bean<?> bean : manager.getBeans(type)) {
            CreationalContext<T> context = (CreationalContext<T>) manager.createCreationalContext(bean);
            if (context != null) {
                result.add((T) manager.getReference(bean, type, context));
            }
        }
        return result;
    }
}
