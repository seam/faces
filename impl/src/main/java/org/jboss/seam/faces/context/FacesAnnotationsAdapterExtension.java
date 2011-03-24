package org.jboss.seam.faces.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;

/**
 * Alias the JSF scope annotations to the CDI scope annotations. If a JSF scope annotation is detected, advise the developer to
 * update the code to use the equivalent CDI scope annotation. Forbid the developer from using the JSF managed bean annotation.
 * 
 * @author Dan Allen
 */
public class FacesAnnotationsAdapterExtension implements Extension {
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
                System.out.println("WARNING: Please annotate class " + annotatedType.getAnnotatedType().getJavaClass()
                        + " with @" + scopeAliasMapping.get(scope).getName() + " instead of @" + scope.getName());
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
        final Annotation cdiScopeAnnotation = new Annotation() {
            public Class<? extends Annotation> annotationType() {
                return cdiScope;
            }
        };

        final Set<Annotation> maskedAnnotations = new HashSet<Annotation>(type.getAnnotations());
        maskedAnnotations.remove(type.getAnnotation(jsfScope));
        maskedAnnotations.add(cdiScopeAnnotation);

        return new AnnotatedType<Object>() {
            public Class<Object> getJavaClass() {
                return type.getJavaClass();
            }

            public Set<AnnotatedConstructor<Object>> getConstructors() {
                return type.getConstructors();
            }

            public Set<AnnotatedMethod<? super Object>> getMethods() {
                return type.getMethods();
            }

            public Set<AnnotatedField<? super Object>> getFields() {
                return type.getFields();
            }

            public Type getBaseType() {
                return type.getBaseType();
            }

            public Set<Type> getTypeClosure() {
                return type.getTypeClosure();
            }

            @SuppressWarnings("unchecked")
            public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
                if (annotationType == jsfScope) {
                    return null;
                } else if (annotationType == cdiScope) {
                    return (T) cdiScopeAnnotation;
                }

                return type.getAnnotation(annotationType);
            }

            public Set<Annotation> getAnnotations() {
                return maskedAnnotations;
            }

            public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType) {
                if (annotationType == jsfScope) {
                    return false;
                } else if (annotationType == cdiScope) {
                    return true;
                }
                return type.isAnnotationPresent(annotationType);
            }
        };
    }
}