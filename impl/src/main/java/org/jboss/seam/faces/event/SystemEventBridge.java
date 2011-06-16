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

import java.lang.annotation.Annotation;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.inject.Inject;

import org.jboss.seam.faces.event.qualifier.Component;
import org.jboss.seam.faces.event.qualifier.View;

/**
 * A SystemEventListener used to bridge JSF system events to the CDI event model.
 * <p/>
 * <p/>
 * For each JSF system event (e.g: {@link PostConstructApplicationEvent}, a corresponding Seam CDI event will be fired.
 * <p/>
 * <p/>
 * Event listeners can be registered by observing the appropriate Seam CDI event (see @{@link Observes}):
 * <p/>
 * <b>For example:</b>
 * <p/>
 * <code>
 * public void listener(@Observes org.jboss.seam.faces.event.qualifier.ExceptionQueuedEvent event)
 * {
 * //do something
 * }
 * </code>
 *
 * @author Nicklas Karlsson
 */
public class SystemEventBridge implements SystemEventListener {
    @Inject
    BeanManager beanManager;

    public boolean isListenerForSource(final Object source) {
        return true;
    }

    public void processEvent(final SystemEvent e) throws AbortProcessingException {
        Object payload = e.getClass().cast(e);
        Annotation[] qualifiers = getQualifiers(e);
        beanManager.fireEvent(payload, qualifiers);
    }

    private Annotation[] getQualifiers(final SystemEvent e) {
        if (isViewEvent(e)) {
            String id = ((UIViewRoot) e.getSource()).getViewId();
            return new Annotation[]{new ViewLiteral(id)};
        } else if (e instanceof ComponentSystemEvent) {
            String id = ((ComponentSystemEvent) e).getComponent().getId();
            return new Annotation[]{new ComponentLiteral(id)};
        } else {
            return new Annotation[]{};
        }
    }

    private boolean isViewEvent(final SystemEvent e) {
        return (e instanceof PreRenderViewEvent) || (e instanceof PostConstructViewMapEvent)
                || (e instanceof PreDestroyViewMapEvent);
    }

    private class ComponentLiteral extends AnnotationLiteral<Component> implements Component {
        private static final long serialVersionUID = -180390717920002323L;

        private String value = "";

        public String value() {
            return value;
        }

        public ComponentLiteral(final String value) {
            if (value != null) {
                this.value = value;
            }
        }
    }

    private class ViewLiteral extends AnnotationLiteral<View> implements View {
        private static final long serialVersionUID = -9101103836360031181L;
        private String value = "";

        public String value() {
            return value;
        }

        public ViewLiteral(final String value) {
            if (value != null) {
                this.value = value;
            }
        }
    }

}
