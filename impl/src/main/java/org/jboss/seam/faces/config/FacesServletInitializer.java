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
package org.jboss.seam.faces.config;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.jboss.logging.Logger;

/**
 * Automatically registers the JSF FacesServlet if it's not already configured by the application (WEB-INF/web.xml).
 * <p/>
 * Only activates in a Servlet 3.0 environment. Most Java EE 6 application servers will register FacesServlet automatically, but
 * in some cases require certain resources to be present in the application (i.e., a JSF component class or
 * WEB-INF/faces-config.xml). Since the application is using Seam Faces, we assume intent to use JSF.
 *
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class FacesServletInitializer implements ServletContainerInitializer {
    private transient final Logger log = Logger.getLogger(FacesServletInitializer.class.getName());

    private static final String FACES_SERVLET_CLASS_NAME = "javax.faces.webapp.FacesServlet";

    private static final String FACES_SERVLET_NAME = "FacesServlet";

    private static final String[] FACES_SERVLET_MAPPINGS = new String[]{"/faces/*", "*.jsf", "*.faces"};

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        if (!isFacesServletConfigured(ctx)) {
            registerFacesServlet(ctx);
        }
    }

    private void registerFacesServlet(ServletContext ctx) {
        log.info("Auto-registering FacesServlet with mappings: " + Arrays.asList(FACES_SERVLET_MAPPINGS));
        ServletRegistration.Dynamic facesServlet = ctx.addServlet(FACES_SERVLET_NAME, FACES_SERVLET_CLASS_NAME);
        facesServlet.addMapping(FACES_SERVLET_MAPPINGS);
    }

    private boolean isFacesServletConfigured(ServletContext ctx) {
        Map<String, ? extends ServletRegistration> servletRegistrations = ctx.getServletRegistrations();
        for (ServletRegistration registration : servletRegistrations.values()) {
            log.debugf("Servlet %s is registered", registration.getClassName());
            if (FACES_SERVLET_CLASS_NAME.equals(registration.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
