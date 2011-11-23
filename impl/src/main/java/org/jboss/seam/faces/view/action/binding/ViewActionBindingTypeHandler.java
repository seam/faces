package org.jboss.seam.faces.view.action.binding;

import static org.jboss.seam.faces.view.action.PhaseInstant.BEFORE_RENDER_RESPONSE;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.faces.view.action.AnnotatedMethodViewActionHandler;
import org.jboss.seam.faces.view.action.PhaseInstant;
import org.jboss.seam.faces.view.action.ViewActionHandlerUtils;

public class ViewActionBindingTypeHandler extends AnnotatedMethodViewActionHandler {
    private PhaseInstant phaseInstant;
    private Integer order;

    public ViewActionBindingTypeHandler(AnnotatedMethod<?> annotatedMethod, Annotation annotation, BeanManager beanManager) {
        super(annotatedMethod, beanManager);
        order = ViewActionHandlerUtils.getOrder(annotation);
        this.phaseInstant = ViewActionHandlerUtils.getPhaseInstantOrDefault(
                Arrays.asList(annotation.annotationType().getAnnotations()), annotation.annotationType(),
                BEFORE_RENDER_RESPONSE);
    }

    @Override
    public boolean handles(PhaseInstant phaseInstant) {
        return this.phaseInstant.equals(phaseInstant);
    }

    @Override
    public Integer getOrder() {
        return order;
    }
}
