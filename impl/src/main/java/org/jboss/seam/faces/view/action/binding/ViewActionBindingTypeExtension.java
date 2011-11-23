package org.jboss.seam.faces.view.action.binding;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.seam.faces.security.RestrictViewActionExtension;
import org.jboss.seam.faces.view.action.ViewActionHandler;
import org.jboss.seam.faces.view.config.ViewConfigDescriptor;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.solder.logging.Logger;

/**
 * Scans for viewController classes and view actions.
 * 
 * @author Adriàn Gonzalez
 */
public class ViewActionBindingTypeExtension implements Extension {
    private final Logger log = Logger.getLogger(RestrictViewActionExtension.class);
    private final Map<Object, List<ViewActionHandler>> viewActionHandlers = new HashMap<Object, List<ViewActionHandler>>();

    public void setup(@Observes AfterDeploymentValidation event, Instance<ViewConfigStore> viewConfigStore, BeanManager beanManager) {
        if (viewConfigStore.isUnsatisfied()) {
            // extension disable : surely because we're in come UT context (i.e. ProjectStageExtensionTest)
            // not adding ViewConfigStore in ShrinkWrap archive
            log.warn("ViewConfigStore dependency missing - RestrictViewActionExtension disabled");
            return;
        }
        registerViewActionBindingTypes(viewConfigStore.get(), beanManager);
    }

    private void registerViewActionBindingTypes(ViewConfigStore viewConfigStore, BeanManager beanManager) {
        List<ViewConfigDescriptor> viewConfigDescriptors = viewConfigStore.getViewConfigDescriptors();
        for (ViewConfigDescriptor viewConfigDescriptor : viewConfigDescriptors) {
            for (Object value : viewConfigDescriptor.getValues()) {
                if (viewActionHandlers.containsKey(value)) {
                    for (ViewActionHandler viewActionHandler : viewActionHandlers.get(value)) {
                        viewConfigDescriptor.addViewActionHandler(viewActionHandler);
                    }
                }
            }
        }
    }

    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event, BeanManager beanManager) {
        AnnotatedType<T> tp = event.getAnnotatedType();
        for (final AnnotatedMethod<?> m : tp.getMethods()) {
            for (final Annotation annotation : m.getAnnotations()) {
                if (annotation.annotationType().isAnnotationPresent(ViewActionBindingType.class)) {
                    ViewActionHandler viewActionHandler = newViewActionHandler(m, annotation, beanManager);
                    Object viewConfigValue = getValue(annotation);
                    if (viewConfigValue == null) {
                        throw new IllegalArgumentException("Annotation " + annotation + " invalid : no view specified");
                    }
                    registerViewActionHandler(viewConfigValue, viewActionHandler);
                }
            }
        }
    }

    protected ViewActionHandler newViewActionHandler(AnnotatedMethod<?> m, Annotation annotation, BeanManager beanManager) {
        return new ViewActionBindingTypeHandler(m, annotation, beanManager);
    }

    protected void registerViewActionHandler(Object viewConfigValue, ViewActionHandler viewActionHandler) {
        List<ViewActionHandler> actions = viewActionHandlers.get(viewConfigValue);
        if (actions == null) {
            actions = new ArrayList<ViewActionHandler>();
            viewActionHandlers.put(viewConfigValue, actions);
        }
        actions.add(viewActionHandler);
    }

    protected Map<Object, List<ViewActionHandler>> getViewActionHandlers() {
        return viewActionHandlers;
    }

    /**
     * Retrieve the view defined by the value() method in the annotation
     * 
     * @param annotation
     * @return the result of value() call
     * @throws IllegalArgumentException if no value() method was found
     */
    private Object getValue(Annotation annotation) {
        Method valueMethod;
        try {
            valueMethod = annotation.annotationType().getDeclaredMethod("value");
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("value method must be declared and must resolve to a valid view", ex);
        } catch (SecurityException ex) {
            throw new IllegalArgumentException("value method must be accessible", ex);
        }
        try {
            return valueMethod.invoke(annotation);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("value method must be accessible", ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

}
