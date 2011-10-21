/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.faces.conversion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

/**
 * @author <a href="http://community.jboss.org/people/LightGuard">Jason Porter</a>a>
 */
@ConversationScoped
@FacesConverter("org.jboss.seam.faces.conversion.ObjectConverter")
public class ObjectConverter implements javax.faces.convert.Converter, Serializable {

    private static final long serialVersionUID = -406332789399557968L;
    final private Map<String, Object> converterMap = new HashMap<String, Object>();

    private int incrementor = 0;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return this.converterMap.get(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        final String incrementorStringValue = String.valueOf(this.incrementor++);
        this.converterMap.put(incrementorStringValue, value);
        return incrementorStringValue;
    }
}
