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

/**
 * A context that lives from Restore View to the next Render Response.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
public interface RenderContext {

    /**
     * Returns true if the current {@link RenderContext} contains no data.
     */
    boolean isEmpty();

    /**
     * Return the current ID of this request's {@link RenderContext}. If the ID has not yet been set as part of a redirect, the
     * ID will be null.
     */
    Integer getId();

    /**
     * Get a key value pair from the {@link RenderContext}.
     */
    Object get(String key);

    /**
     * Put a key value pair into the {@link RenderContext}.
     */
    void put(String key, Object value);

}
