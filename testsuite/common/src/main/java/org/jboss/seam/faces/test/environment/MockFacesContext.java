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
package org.jboss.seam.faces.test.environment;

import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;

/**
 * @author Dan Allen
 */
public class MockFacesContext extends FacesContext {
    private PhaseId currentPhaseId;
    private ExternalContext externalContext = new MockExternalContext();

    public FacesContext set() {
        setCurrentInstance(this);
        return this;
    }

    @Override
    public void release() {
        setCurrentInstance(null);
    }

    @Override
    public PhaseId getCurrentPhaseId() {
        return currentPhaseId;
    }

    @Override
    public void setCurrentPhaseId(PhaseId currentPhaseId) {
        this.currentPhaseId = currentPhaseId;
    }

    @Override
    public Application getApplication() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Iterator<String> getClientIdsWithMessages() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ExternalContext getExternalContext() {
        return externalContext;
    }

    @Override
    public Severity getMaximumSeverity() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Iterator<FacesMessage> getMessages() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Iterator<FacesMessage> getMessages(String clientId) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public RenderKit getRenderKit() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean getRenderResponse() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean getResponseComplete() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ResponseStream getResponseStream() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setResponseStream(ResponseStream stream) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ResponseWriter getResponseWriter() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setResponseWriter(ResponseWriter writer) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public UIViewRoot getViewRoot() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setViewRoot(UIViewRoot uivr) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void addMessage(String clientId, FacesMessage message) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void renderResponse() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void responseComplete() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
