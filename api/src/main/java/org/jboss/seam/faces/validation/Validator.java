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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * <p>
 * A generic abstract class implementing Validator, for convenient removal of type casting.
 * </p>
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public abstract class Validator<T> implements javax.faces.validator.Validator {
    FacesContext context;

    public abstract void validate(UIComponent component, T value) throws ValidatorException;

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws javax.faces.validator.ValidatorException {
        this.context = context;
        validate(component, (T) value);
    }

    public FacesContext getContext() {
        return context;
    }
}
