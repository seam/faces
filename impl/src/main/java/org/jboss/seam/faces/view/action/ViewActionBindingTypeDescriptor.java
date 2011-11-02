package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.enterprise.inject.spi.AnnotatedMethod;

import static org.jboss.seam.faces.view.action.PhaseInstant.BEFORE_RENDER_RESPONSE;

public class ViewActionBindingTypeDescriptor {
    private Annotation annotation;
    private Object viewControllerValue;
    private AnnotatedMethod<?> annotatedMethod;
    private PhaseInstant phaseInstant;

    public ViewActionBindingTypeDescriptor(AnnotatedMethod<?> annotatedMethod, Annotation annotation,
            Object viewControllerValue) {
        this.annotatedMethod = annotatedMethod;
        this.annotation = annotation;
        this.viewControllerValue = viewControllerValue;
        this.phaseInstant = ViewActionUtils.getPhaseInstantOrDefault(Arrays.asList(annotation.annotationType().getAnnotations()),
                annotation.annotationType(), BEFORE_RENDER_RESPONSE);
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public Object getViewControllerValue() {
        return viewControllerValue;
    }

    public void setViewControllerValue(Object viewControllerValue) {
        this.viewControllerValue = viewControllerValue;
    }

    public AnnotatedMethod<?> getAnnotatedMethod() {
        return annotatedMethod;
    }

    public void setAnnotatedMethod(AnnotatedMethod<?> annotatedMethod) {
        this.annotatedMethod = annotatedMethod;
    }

    public PhaseInstant getPhaseInstant() {
        return phaseInstant;
    }
}
