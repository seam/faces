package org.jboss.seam.faces.view.action.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.seam.faces.view.action.AnnotatedMethodViewActionHandler;
import org.jboss.seam.faces.view.action.PhaseInstant;
import org.jboss.seam.faces.view.action.ViewActionHandler;
import org.jboss.seam.faces.view.action.ViewActionHandlerProvider;
import org.jboss.seam.faces.view.action.ViewActionHandlerUtils;

public class ViewControllerHandlerProvider implements ViewActionHandlerProvider<ViewController> {

    @Inject
    private BeanManager beanManager;
    private List<Class<?>> viewControllerClasses = new ArrayList<Class<?>>();
    private List<ViewActionHandler> viewActionHandlers = new ArrayList<ViewActionHandler>();

    @Override
    public void initialize(ViewController annotation) {
        this.viewControllerClasses = Arrays.asList(annotation.value());
        for (Class<?> viewControllerClass : viewControllerClasses) {
            Class<?> current = viewControllerClass;
            while (current != Object.class) {
                for (Method method : current.getDeclaredMethods()) {
                    PhaseInstant phaseInstant = ViewActionHandlerUtils.getPhaseInstant(Arrays.asList(method.getAnnotations()),
                            method);
                    if (phaseInstant != null) {
                        viewActionHandlers.add(new ViewControllerHandler(method, beanManager));
                    }
                }
                current = current.getSuperclass();
            }
        }
    }

    @Override
    public List<ViewActionHandler> getActionHandlers() {
        return viewActionHandlers;
    }

    /**
     * Invokes method on a CDI bean.
     * 
     * Note : copy from Seam Security's SecurityExtension class. Should be extracted into common utility.
     */
    private static class ViewControllerHandler extends AnnotatedMethodViewActionHandler {

        private PhaseInstant phaseInstant;

        public ViewControllerHandler(Method method, BeanManager beanManager) {
            super(null, beanManager);
            setAnnotatedMethod(convert(method));
            phaseInstant = ViewActionHandlerUtils.getPhaseInstant(Arrays.asList(method.getAnnotations()), method);
        }

        @Override
        public boolean handles(PhaseInstant phaseInstant) {
            return this.phaseInstant.equals(phaseInstant);
        }

        private AnnotatedMethod<?> convert(Method method) {
            AnnotatedType<?> annotatedType = getBeanManager().createAnnotatedType(method.getDeclaringClass());
            AnnotatedMethod<?> annotatedMethod = null;
            for (AnnotatedMethod<?> current : annotatedType.getMethods()) {
                if (current.getJavaMember().equals(method)) {
                    annotatedMethod = current;
                }
            }
            if (annotatedMethod == null) {
                throw new IllegalStateException("No matching annotated method found for method : " + method);
            }
            return annotatedMethod;
        }

        @Override
        public Integer getOrder() {
            return null;
        }
    }
}
