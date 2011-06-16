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
import java.util.List;
import java.util.Map;

/**
 * stores data specific to a given view is a hierarchical fashion
 *
 * @author Stuart Douglas
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public interface ViewConfigStore {

    /**
     * Adds data to the store
     *
     * @param viewId     The view id to associate the data with. A * at the end of the view id is considered a wildcard
     * @param annotation the data to store
     */
    public abstract void addAnnotationData(String viewId, Annotation annotation);

    /**
     * gets the most specific data for a given viewId
     */
    public abstract <T extends Annotation> T getAnnotationData(String viewId, Class<T> type);

    /**
     * returns all data for a given viewId, with the most specific data at the start of the list
     */
    public abstract <T extends Annotation> List<T> getAllAnnotationData(String viewId, Class<T> type);

    /**
     * returns all qualified data for a given viewId, with the most specific data at the start of the list
     */
    public abstract List<? extends Annotation> getAllQualifierData(String viewId, Class<? extends Annotation> qualifier);

    /**
     * return a map of views to annotations for a given annotation type
     */
    public <T extends Annotation> Map<String, Annotation> getAllAnnotationViewMap(Class<T> type);

}
