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
package org.jboss.seam.faces.rewrite;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletContext;

import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.config.servlet.WebXmlParser;
import com.ocpsoft.pretty.faces.spi.ConfigurationProvider;
import org.jboss.seam.faces.beanManager.BeanManagerServletContextListener;
import org.jboss.seam.faces.util.BeanManagerUtils;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.solder.core.Requires;
import org.xml.sax.SAXException;


/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Requires("com.ocpsoft.pretty.faces.spi.ConfigurationProvider")
public class RewriteConfiguration implements ConfigurationProvider {
    public static final String PRETTYFACES_CONFIG_SERVLETCONTEXT_KEY = "org.jboss.seam.faces.com.ocpsoft.pretty.faces.spi.ConfigurationProvider";

    @Override
    public PrettyConfig loadConfiguration(ServletContext sc) {
        WebXmlParser webXmlParser = new WebXmlParser();
        try {
            webXmlParser.parse(sc);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SAXException ex) {
            throw new RuntimeException(ex);
        }
        BeanManager beanManager = (BeanManager) sc.getAttribute(BeanManagerServletContextListener.BEANMANAGER_SERVLETCONTEXT_KEY);
        ViewConfigStore store = BeanManagerUtils.getContextualInstance(beanManager, ViewConfigStore.class);
        List<UrlMapping> mappings = loadUrlMappings(store, webXmlParser.getFacesMapping());
        PrettyConfig prettyConfig = new PrettyConfig();
        prettyConfig.setMappings(mappings);
        return prettyConfig;
    }

    private List<UrlMapping> loadUrlMappings(ViewConfigStore store, String facesMapping) {
        List<UrlMapping> mappings = new ArrayList<UrlMapping>();
        Map<String, Annotation> map = store.getAllAnnotationViewMap(org.jboss.seam.faces.rewrite.UrlMapping.class);
        if (map != null) {
            for (Map.Entry<String, Annotation> entry : map.entrySet()) {
                mappings.add(buildPrettyFacesUrlMapping(entry.getKey(), entry.getValue(), facesMapping));
            }
        }
        return mappings;
    }

    private UrlMapping buildPrettyFacesUrlMapping(String viewId, Annotation annotation, String facesMapping) {
        org.jboss.seam.faces.rewrite.UrlMapping urlMappingAnnotation = (org.jboss.seam.faces.rewrite.UrlMapping) annotation;
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setViewId(buildViewUrl(viewId, facesMapping));
        urlMapping.setId(viewId);
        urlMapping.setOnPostback(urlMappingAnnotation.onPostback());
        urlMapping.setOutbound(urlMappingAnnotation.outbound());
        urlMapping.setPattern(urlMappingAnnotation.pattern());
        return urlMapping;
    }

    String buildViewUrl(String viewId, String facesMapping) {
        String viewUrl = null;
        if (facesMapping.endsWith("*")) {
            viewUrl = facesMapping.replaceFirst("\\*", viewId);
        } else if (facesMapping.startsWith("*")) {
            String[] splits = viewId.split("\\.");
            String viewName;
            if (splits.length == 1) {
                viewName = viewId;
            } else {
                viewName = splits[0];
            }
            viewUrl = facesMapping.replaceFirst("\\*", viewName);
        }
        if (!viewUrl.startsWith("/")) {
            viewUrl = "/" + viewUrl;
        }
        return viewUrl;
    }

}
