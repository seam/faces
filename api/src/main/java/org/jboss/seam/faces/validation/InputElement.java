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
package org.jboss.seam.faces.validation;

import java.io.Serializable;

import javax.faces.component.UIInput;

import org.jboss.seam.solder.core.Veto;

/**
 * To be used in conjunction with <code>&lt;s:validateForm /&gt;</code> in Validators that should have their values fetched from
 * a JSF form field.
 * <p/>
 * Example of injection:
 *
 * @param <T> is the type of value the inputElement holds.
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 * @Inject private InputElement<String> firstName;
 * @Inject private InputElement<String> lastName;
 * @Inject private InputElement<Date> dateOfBirth;
 */
@Veto
public class InputElement<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String clientId;
    private final UIInput component;
    private Object value;

    public InputElement(String id, String clientId, UIInput component) {
        super();
        this.id = id;
        this.clientId = clientId;
        this.component = component;
    }

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public UIInput getComponent() {
        return component;
    }

    public T getValue() {
        return (T) value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
