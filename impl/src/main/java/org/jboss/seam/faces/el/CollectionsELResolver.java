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
package org.jboss.seam.faces.el;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ELResolver;

import org.jboss.solder.el.Resolver;
import org.jboss.solder.reflection.Reflections;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Resolver
public class CollectionsELResolver extends ELResolver {
    private static Class<?> DATA_MODEL;
    private static Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    static {
        try {
            DATA_MODEL = CollectionsELResolver.class.getClassLoader().loadClass("javax.faces.model.DataModel");
        } catch (ClassNotFoundException e) {
            DATA_MODEL = null;
        }
    }

    // private ELResolver getWrapped() {
    // return FacesContext.getCurrentInstance().getELContext().getELResolver();
    // }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (base instanceof Collection) {
            return this.resolveInCollection(context, (Collection) base, property);
        } else if (base instanceof Map) {
            return this.resolveInMap(context, (Map) base, property);
        } else if (DATA_MODEL.isInstance(base)) {
            return this.resolveInDataModel(context, base, property);
        } else if (base != null) {
            return this.resolveNoArgMethod(context, base, property.toString());
        }
        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return null;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        return;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return base != null && (base.getClass().isInstance(DATA_MODEL) || base instanceof Collection || base instanceof Map);
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return Object.class;
    }

    private Object resolveInMap(ELContext context, Map map, Object property) {

        try {
            if ("size".equals(property) && !map.containsKey("size")) {
                int size = map.size();
                context.isPropertyResolved();
                context.setPropertyResolved(true);
                return size;
            } else if ("values".equals(property) && !map.containsKey("values")) {
                Collection<?> values = map.values();
                context.setPropertyResolved(true);
                return values;
            } else if ("keySet".equals(property) && !map.containsKey("keySet")) {
                Set<?> keySet = map.keySet();
                context.setPropertyResolved(true);
                return keySet;
            } else if ("entrySet".equals(property) && !map.containsKey("entrySet")) {
                Set<?> entrySet = map.entrySet();
                context.setPropertyResolved(true);
                return entrySet;
            } else if ("empty".equals(property) && !map.containsKey("empty")) {
                boolean empty = map.isEmpty();
                context.setPropertyResolved(true);
                return empty;
            }
        } catch (Exception e) {
            // swallowed: we aren't resolving anything if this happens
        }

        return null;
    }

    private Object resolveInDataModel(ELContext context, Object base, Object property) {
        if ("size".equals(property)) {
            context.setPropertyResolved(true);
            return Reflections.invokeMethod(Reflections.findDeclaredMethod(DATA_MODEL, "getRowCount", EMPTY_CLASS_ARRAY), base);
        } else if ("empty".equals(property)) {
            context.setPropertyResolved(true);
            return (Integer) Reflections.invokeMethod(
                    Reflections.findDeclaredMethod(DATA_MODEL, "getRowCount", EMPTY_CLASS_ARRAY), base) == 0;
        } else {
            return null;
        }
    }

    private Object resolveInCollection(ELContext context, Collection collection, Object property) {
        if ("size".equals(property)) {
            context.setPropertyResolved(true);
            return collection.size();
        } else {
            return null;
        }
    }

    private Object resolveNoArgMethod(ELContext context, Object target, String property) {
        final Method foundMethod = Reflections.findDeclaredMethod(target.getClass(), property, EMPTY_CLASS_ARRAY);
        if (foundMethod != null) {
            context.setPropertyResolved(true);
            return Reflections.invokeMethod(foundMethod, target);
        }
        return null;
    }

    public static Object invok(Method method, Object target, Object... args) {
        return Reflections.invokeMethod(method, target, args);
    }

}
