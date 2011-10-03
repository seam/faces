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
package org.jboss.seam.faces.context;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;

import org.jboss.seam.logging.Logger;
import org.jboss.solder.reflection.AnnotationInstanceProvider;
import org.jboss.solder.reflection.annotated.AnnotatedTypeBuilder;

/**
 * Alias the JSF scope annotations to the CDI scope annotations. If a JSF scope annotation is detected, advise the developer to
 * update the code to use the equivalent CDI scope annotation. Forbid the developer from using the JSF managed bean annotation.
 *
 * @author Dan Allen
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class FacesAnnotationsAdapterExtension implements Extension {
    private transient final Logger logger = Logger.getLogger(FacesAnnotationsAdapterExtension.class.getName());
    private final Map<Class<? extends Annotation>, Class<? extends Annotation>> scopeAliasMapping;

    /*
     * For unit testing
     */
    private static final Map<Class<?>, Class<? extends Annotation>> aliasedBeans = new HashMap<Class<?>, Class<? extends Annotation>>();

    public static Map<Class<?>, Class<? extends Annotation>> getAliasedbeans() {
        return aliasedBeans;
    }

    public FacesAnnotationsAdapterExtension() {
        scopeAliasMapping = new HashMap<Class<? extends Annotation>, Class<? extends Annotation>>(3);
        scopeAliasMapping.put(javax.faces.bean.RequestScoped.class, javax.enterprise.context.RequestScoped.class);
        scopeAliasMapping.put(javax.faces.bean.SessionScoped.class, javax.enterprise.context.SessionScoped.class);
        scopeAliasMapping.put(javax.faces.bean.ApplicationScoped.class, javax.enterprise.context.ApplicationScoped.class);
    }

    public void aliasJsfScopeIfDetected(@Observes final ProcessAnnotatedType<Object> annotatedType) {
        for (Class<? extends Annotation> scope : scopeAliasMapping.keySet()) {
            if (annotatedType.getAnnotatedType().isAnnotationPresent(scope)) {
                logger.warnf("WARNING: Please annotate class %s with @%s instead of @%s", annotatedType.getAnnotatedType().getJavaClass(), scopeAliasMapping.get(scope).getName(), scope.getName());
                aliasedBeans.put(annotatedType.getAnnotatedType().getJavaClass(), scope);
                annotatedType.setAnnotatedType(decorateType(annotatedType.getAnnotatedType(), scope));
                break;
            }
        }
    }

    public void failIfJsfManagedBeanAnnotationPresent(@Observes final ProcessBean<?> bean) {
        if (bean.getAnnotated().isAnnotationPresent(javax.faces.bean.ManagedBean.class)) {
            bean.addDefinitionError(new RuntimeException(
                    "Use of @javax.faces.bean.ManagedBean is forbidden. Please use @javax.inject.Named instead."));
        }
    }

    private Class<? extends Annotation> getCdiScopeFor(final Class<? extends Annotation> jsfScope) {
        return scopeAliasMapping.get(jsfScope);
    }

    private AnnotatedType<Object> decorateType(final AnnotatedType<Object> type, final Class<? extends Annotation> jsfScope) {
        final Class<? extends Annotation> cdiScope = getCdiScopeFor(jsfScope);

        AnnotationInstanceProvider provider = new AnnotationInstanceProvider();
        final Annotation cdiScopeAnnotation = provider.get(cdiScope, Collections.EMPTY_MAP);

        AnnotatedTypeBuilder builder;
        builder = new AnnotatedTypeBuilder()
                .readFromType(type)
                .removeFromClass(jsfScope)
                .addToClass(cdiScopeAnnotation);
        return builder.create();
    }
}
