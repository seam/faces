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
package org.jboss.seam.faces.environment;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 * <p>
 * A producer which retrieves the {@link Flash} for the current request of the JavaServer Faces application by calling
 * {@link FacesContext#getCurrentInstance()} and stores the result as a request-scoped bean instance.
 * </p>
 * <p/>
 * <p>
 * This producer allows the {@link Flash} to be injected:
 * </p>
 * <p/>
 * <pre>
 * &#064;Inject
 * Flash flash;
 * </pre>
 *
 * @author Lincoln Baxter
 */
public class FacesFlashProducer {
    @Produces
    @RequestScoped
    public Flash getFlash() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            throw new ContextNotActiveException("FacesContext is not active");
        }

        Flash ctx = facesContext.getExternalContext().getFlash();
        if (ctx == null) {
            throw new ContextNotActiveException("Flash is not active");
        }

        return ctx;
    }
}
