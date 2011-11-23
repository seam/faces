package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;

/**
 * This support class handles order and phaseInstant retrieval from annotation attributes or meta-annotations.
 * 
 * @author Adriàn Gonzalez
 */
public abstract class ViewActionHandlerProviderSupport<X extends Annotation> implements ViewActionHandlerProvider<X> {
    private PhaseInstant phaseInstant;
    private Integer order;

    @Override
    public final void initialize(X annotation) {
        setPhaseInstant(ViewActionHandlerUtils.getPhaseInstantOrDefault(annotation, PhaseInstant.BEFORE_RENDER_RESPONSE));
        setOrder(ViewActionHandlerUtils.getOrder(annotation));
        doInitialize(annotation);
    }

    protected abstract void doInitialize(X annotation);

    protected PhaseInstant getPhaseInstant() {
        return phaseInstant;
    }

    protected void setPhaseInstant(PhaseInstant phaseInstant) {
        this.phaseInstant = phaseInstant;
    }

    public Integer getOrder() {
        return order;
    }

    protected void setOrder(Integer order) {
        this.order = order;
    }
}
