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
package org.jboss.seam.faces.test.weld.context;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Typed;

import org.jboss.seam.faces.context.RenderContext;

/**
 * A mock {@link RenderContext} that can be injected into tests.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@Typed(RenderContext.class)
public class MockFlashContext implements RenderContext, Serializable {
    private static final long serialVersionUID = 7502050909452181348L;
    private Integer id = null;
    private final Map<String, Object> map = new ConcurrentHashMap<String, Object>();

    public Object get(final String key) {
        return map.get(key);
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void put(final String key, final Object value) {
        map.put(key, value);
    }

}
