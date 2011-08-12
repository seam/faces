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
package org.jboss.seam.faces.navigation;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allows for controlling the redirect to a new page with paramaters and altering the existing conversation
 * 
 * @author Cody Lerum
 * 
 */
public class FacesManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HttpServletResponse response;

    @Inject
    private HttpServletRequest request;

    @Inject
    private Conversation conversation;

    private boolean endConversation;
    private boolean includeConversationId;
    private String viewId;
    private Map<String, Object> parameters;

    /**
     * Execute the redirect
     * 
     * @throws IOException
     */
    public void redirect() throws IOException {
        if (viewId == null) {
            throw new RuntimeException("ViewID was not set");
        }

        if (includeConversationId) {
            parameters.put("cid", conversation.getId());
        }

        String encodedParameters = encodeParameters();

        if (endConversation && !conversation.isTransient()) {
            conversation.end();
        }

        if (encodedParameters != null) {
            response.sendRedirect(request.getContextPath() + viewId + encodedParameters);
        } else {
            response.sendRedirect(request.getContextPath() + viewId);
        }

        resetToDefaults();
    }

    /**
     * Redirect the to an external URL. Not to a view-id
     * 
     * @param url
     * @throws IOException
     */
    public void redirectToExternalURL(String url) throws IOException {
        response.sendRedirect(url);
    }

    /**
     * Set the ViewId
     * 
     * @param viewId
     * @return
     */
    public FacesManager viewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    /**
     * End the existing Conversation if it is long running.
     * 
     * @return
     */
    public FacesManager endConversation() {
        endConversation = true;
        return this;
    }

    /**
     * Include the existing Conversation id "cid" in the viewId
     * 
     * @return
     */
    public FacesManager includeConversationId() {
        includeConversationId = true;
        return this;
    }

    /**
     * Add a name/value pair to the viewId. Example home.xhtml?oid=6
     * 
     * @param key
     * @param value
     * @return
     */
    public FacesManager addParam(String name, Object value) {
        if (parameters == null) {
            parameters = new HashMap<String, Object>();
        }

        parameters.put(name, value);
        return this;
    }

    /**
     * Add a Map<String,Object> of params to the viewId
     * 
     * @param params
     * @return
     */
    public FacesManager addParam(Map<String, Object> params) {
        if (parameters == null) {
            parameters = new HashMap<String, Object>();
        }

        parameters.putAll(params);
        return this;
    }

    private String encodeParameters() {

        if (parameters == null || parameters.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (Entry<String, Object> p : parameters.entrySet()) {
            if (sb.length() == 0) {
                sb.append("?");
            } else {
                sb.append("&");
            }

            sb.append(p.getKey());
            sb.append("=");
            sb.append(p.getValue().toString());
        }

        return sb.toString();
    }

    private void resetToDefaults() {
        parameters.clear();
        endConversation = false;
        includeConversationId = false;
        viewId = null;
    }
}
