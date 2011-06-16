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

import javax.enterprise.inject.Produces;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * <p>
 * A producer which retrieves the ProjectStage for the current request of the JavaServer Faces application, storing the result
 * as a ApplicationScoped bean instance.
 * </p>
 * <p/>
 * <p>
 * This producer is named, allowing the Project Stage to be accessed via EL:
 * </p>
 * <p/>
 * <pre>
 * #{projectStage}
 * </pre>
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class ProjectStageProducer {
    @Named
    @Produces
    public ProjectStage getProjectStage(final FacesContext context) {
        return context.getApplication().getProjectStage();
    }
}
