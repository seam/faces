package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * Scans for viewController classes and view actions.
 * 
 * @author Adriàn Gonzalez
 */
public class ViewControllerExtension implements Extension {

    private final Map<Object, List<ViewActionBindingTypeDescriptor>> descriptors = new HashMap<Object, List<ViewActionBindingTypeDescriptor>>();

    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        AnnotatedType<T> tp = event.getAnnotatedType();
        for (final AnnotatedMethod<?> m : tp.getMethods()) {
            for (final Annotation annotation : m.getAnnotations()) {
                if (annotation.annotationType().isAnnotationPresent(ViewActionBindingType.class)) {
                    Object viewConfigValue = getValue(annotation);
                    if (viewConfigValue == null) {
                        throw new IllegalArgumentException("Annotation " + annotation
                                + " invalid : no view specified");
                    }
                    List<ViewActionBindingTypeDescriptor> actions = descriptors.get(viewConfigValue);
                    if (actions == null) {
                        actions = new ArrayList<ViewActionBindingTypeDescriptor>();
                        descriptors.put(viewConfigValue, actions);
                    }
                    ViewActionBindingTypeDescriptor descriptor = new ViewActionBindingTypeDescriptor(m, annotation, viewConfigValue);
                    actions.add(descriptor);
                }
            }
        }
    }

    public Map<Object, List<ViewActionBindingTypeDescriptor>> getViewActionBindingTypeDescriptors() {
        return descriptors;
    }

    
    // Utility methods for viewAction, TODO move this block out of this class

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
