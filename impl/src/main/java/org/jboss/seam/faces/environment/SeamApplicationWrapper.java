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
package org.jboss.seam.faces.environment;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;
import javax.inject.Inject;

import org.jboss.seam.faces.util.BeanManagerUtils;
import org.jboss.solder.logging.Logger;

/**
 * Provides contextual lifecycle and @{link Inject} support for JSF artifacts such as {@link Converter}, {@link Validator}.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@ApplicationScoped
public class SeamApplicationWrapper extends ApplicationWrapper {
    private transient final Logger log = Logger.getLogger(SeamApplicationWrapper.class);

    private Application parent;

    @Inject
    BeanManagerUtils managerUtils;

    @Override
    public Application getWrapped() {
        return parent;
    }

    @Override
    public Converter createConverter(final Class<?> targetClass) {
        log.debugf("Creating converter for targetClass %s", targetClass.getName());
        Converter result = parent.createConverter(targetClass);
        result = attemptExtension(result);
        return result;
    }

    @Override
    public Converter createConverter(final String converterId) {
        log.debugf("Creating converter for converterId %s", converterId);
        Converter result = parent.createConverter(converterId);
        result = attemptExtension(result);
        return result;
    }

    @Override
    public Validator createValidator(final String validatorId) {
        log.debugf("Creating validator for validatorId %s", validatorId);
        Validator result = parent.createValidator(validatorId);
        result = attemptExtension(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> T attemptExtension(final T base) {
        T result = base;

        if (base == null) {
            return null;
        }

        log.debugf("Extending class: %s", base.getClass().getName());
        if (managerUtils.isDependentScoped(base.getClass())) {
            managerUtils.injectNonContextualInstance(result);
        } else {
            result = (T) managerUtils.getContextualInstance(base.getClass());
        }

        if (result == null) {
            if (!base.getClass().getName().startsWith("javax.")) {
                log.debugf("Using JSF provided instance, unable to find a BeanManaged instance for class %s", base.getClass()
                        .getName());
            }
            result = base;
        }

        return result;
    }

    public Application getParent() {
        return parent;
    }

    public void setParent(Application parent) {
        this.parent = parent;
    }
}
