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
package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.seam.faces.view.action.ViewActionBindingType;
import org.jboss.solder.logging.Logger;

/**
 * Extension that scans enums for view specific configuration
 *
 * @author stuart
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class ViewConfigExtension implements Extension {

    private transient final Logger log = Logger.getLogger(ViewConfigExtension.class);

    private final Map<String, Set<Annotation>> data = new HashMap<String, Set<Annotation>>();

    private final Map<Object,String> viewFieldValueToViewId = new HashMap<Object, String>();

    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        AnnotatedType<T> tp = event.getAnnotatedType();
        if (log.isTraceEnabled()) {
            log.tracef("Annotated Type: %s", tp.getJavaClass().getName());
            for (Annotation annotation : tp.getAnnotations()) {
                log.tracef("|-- Annotation: %s", annotation.annotationType().getName());
                for (Annotation qualifier : annotation.getClass().getAnnotations()) {
                    log.tracef("    |-- Qualifier: %s", qualifier.annotationType().getName());
                }
            }
        }
        if (tp.isAnnotationPresent(ViewConfig.class)) {
            if (!tp.getJavaClass().isInterface()) {
                log.warn("ViewConfig annotation should only be applied to interfaces, and [" + tp.getJavaClass()
                        + "] is not an interface.");
            } else {
                for (Class clazz : tp.getJavaClass().getClasses()) {
                    for (Field enumm : clazz.getFields())
                        if (enumm.isAnnotationPresent(ViewPattern.class)) {
                            ViewPattern viewConfig = enumm.getAnnotation(ViewPattern.class);
                            Set<Annotation> viewPattern = new HashSet<Annotation>();
                            for (Annotation a : enumm.getAnnotations()) {
                                if (a.annotationType() != ViewPattern.class) {
                                    viewPattern.add(a);
                                }
                            }
                            data.put(viewConfig.value(), viewPattern);
                            viewFieldValueToViewId.put(getViewFieldValue(enumm), viewConfig.value());
                        }
                }
            }
        }
        // viewAction processing
        for (final AnnotatedMethod m : tp.getMethods()) {
            for (final Annotation annotation : m.getAnnotations()) {
                if (annotation.annotationType().isAnnotationPresent(ViewActionBindingType.class)) {
                	Object viewField = getValue (annotation);
                	String viewId = viewFieldValueToViewId.get(viewField);
                	if (viewId == null) {
                		throw new IllegalArgumentException("Annotation "+annotation+" invalid : the view specified"
                				+ "("+viewField+") doesn't correspond to a registered view in an annotated @ViewConfig class/interface.");
                	}
                	data.get(viewId).add(annotation);
                }
            }
        }
    }

    /**
     * Returns the value of a view field. 
	 * @throws IllegalArgumentException if an error happens
     */
    private Object getViewFieldValue(Field enumm) {
        try {
			return enumm.get(null);
		} catch (IllegalArgumentException e) {
    		throw new IllegalArgumentException("Invalid view field "+enumm+" - error getting value "+e.toString(), e);
		} catch (IllegalAccessException e) {
    		throw new IllegalArgumentException("Invalid view field "+enumm+" - error getting value "+e.toString(), e);
		}
	}

	public Map<String, Set<Annotation>> getData() {
        return Collections.unmodifiableMap(data);
    }

    
    // Utility methods for viewAction, TODO move this block out of this class
    
    /**
     * Retrieve the view defined by the value() method in the annotation
     *
     * @param annotation
     * @return the result of value() call
     * @throws IllegalArgumentException if no value() method was found
     */
    private Object getValue(Annotation annotation) {
        Method valueMethod;
        try {
        	valueMethod = annotation.annotationType().getDeclaredMethod("value");
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("value method must be declared and must resolve to a valid view", ex);
        } catch (SecurityException ex) {
            throw new IllegalArgumentException("value method must be accessible", ex);
        }
        try {
        	return valueMethod.invoke(annotation);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("value method must be accessible", ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
