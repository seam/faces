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
package org.jboss.seam.faces.beanManager;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jboss.solder.beanManager.BeanManagerLocator;

/**
 * A Servlet Context Listener that places the BeanManager under a known attribute key
 *
 * @author Nicklas Karlsson
 */
public class BeanManagerServletContextListener implements ServletContextListener {

    public static final String BEANMANAGER_SERVLETCONTEXT_KEY = "org.jboss.seam.faces.javax.enterprise.spi.BeanManager";

    @Inject
    private BeanManager beanManager;

    public void contextDestroyed(ServletContextEvent sce) {
    }

    public void contextInitialized(ServletContextEvent sce) {

        // servlet context attribute supported by Weld 1.0.x and OpenWebBeans
        if (beanManager == null) {
            beanManager = (BeanManager) sce.getServletContext().getAttribute(BeanManager.class.getName());
        }

        // servlet context attribute supported by Weld 1.1.x
        if (beanManager == null) {
            beanManager = (BeanManager) sce.getServletContext().getAttribute(
                    "org.jboss.weld.environment.servlet.javax.enterprise.inject.spi.BeanManager");
        }

        // lookup via BeanManagerLocator
        if (beanManager == null) {
            BeanManagerLocator locator = new BeanManagerLocator();
            if (locator.isBeanManagerAvailable()) {
                beanManager = locator.getBeanManager();
            }
        }

        // place BeanManager in the known attribute
        sce.getServletContext().setAttribute(BEANMANAGER_SERVLETCONTEXT_KEY, beanManager);

    }

}
