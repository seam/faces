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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.jboss.solder.beanManager.BeanManagerProvider;

/**
 * A BeanManager provider for the Servlet Context attribute "javax.enterprise.inject.spi.BeanManager"
 *
 * @author Nicklas Karlsson
 */
public class FacesServletContextBeanManagerProvider implements BeanManagerProvider {
    public static final FacesServletContextBeanManagerProvider DEFAULT = new FacesServletContextBeanManagerProvider();

    private static final List<String> SERVLET_CONTEXT_KEYS = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add(BeanManager.class.getName());
            add(BeanManagerServletContextListener.BEANMANAGER_SERVLETCONTEXT_KEY);
        }
    };

    public BeanManager getBeanManager() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return null;
        }
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        return getBeanManager(servletContext);
    }

    private BeanManager getBeanManager(ServletContext servletContext) {
        BeanManager beanManager = null;
        for (String key : SERVLET_CONTEXT_KEYS) {
            beanManager = (BeanManager) servletContext.getAttribute(key);
            if (beanManager != null) {
                break;
            }
        }
        return beanManager;
    }

    public int getPrecedence() {
        return 20;
    }

}
