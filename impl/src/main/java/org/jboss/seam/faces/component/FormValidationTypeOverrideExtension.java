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
package org.jboss.seam.faces.component;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.seam.faces.validation.InputField;
import org.jboss.solder.reflection.annotated.AnnotatedTypeBuilder;

/**
 * Ensure that any field annotated with {@link InputField} is produced by the same producer method with output type
 * {@link Object}.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@ApplicationScoped
public class FormValidationTypeOverrideExtension implements Extension {
    private final Map<Class<?>, AnnotatedType<?>> typeOverrides = new HashMap<Class<?>, AnnotatedType<?>>();

    public <T> void processAnnotatedType(@Observes final ProcessAnnotatedType<T> event) {
        AnnotatedTypeBuilder<T> builder = new AnnotatedTypeBuilder<T>();
        builder.readFromType(event.getAnnotatedType());

        boolean modifiedType = false;

        for (AnnotatedField<?> f : event.getAnnotatedType().getFields()) {
            if (f.isAnnotationPresent(InputField.class)) {
                builder.overrideFieldType(f.getJavaMember(), Object.class);
                modifiedType = true;
            }
        }

        if (modifiedType) {
            AnnotatedType<T> replacement = builder.create();
            typeOverrides.put(replacement.getJavaClass(), replacement);
            event.setAnnotatedType(replacement);
        }
    }

    public boolean hasOverriddenType(final Class<?> clazz) {
        return typeOverrides.containsKey(clazz);
    }

    public AnnotatedType<?> getOverriddenType(final Class<?> clazz) {
        return typeOverrides.get(clazz);
    }
}
