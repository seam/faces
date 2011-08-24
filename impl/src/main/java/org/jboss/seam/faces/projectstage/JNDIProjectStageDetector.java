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
package org.jboss.seam.faces.projectstage;

import javax.faces.application.ProjectStage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Implementation of {@link ProjectStageDetector} that performs a JNDI lookup to get the current project stage.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class JNDIProjectStageDetector implements ProjectStageDetector {

    @Override
    public int getPrecedence() {
        return 50;
    }

    @Override
    public ProjectStage getProjectStage() {

        // try to get the name of the project stage from JNDI
        String projectStageName = getProjectStageNameFromJNDI();

        // lookup the correct enum for the value found via JNDI
        if (projectStageName != null && projectStageName.length() > 0) {
            for (ProjectStage stage : ProjectStage.values()) {
                if (stage.name().equalsIgnoreCase(projectStageName)) {
                    return stage;
                }
            }
        }

        // no result
        return null;

    }

    /**
     * Performs a JNDI lookup to obtain the current project stage. The method use the standard JNDI name for the JSF project
     * stage for the lookup
     * 
     * @return name bound to JNDI or <code>null</code>
     */
    private String getProjectStageNameFromJNDI() {

        try {

            InitialContext context = new InitialContext();
            Object obj = context.lookup(ProjectStage.PROJECT_STAGE_JNDI_NAME);
            if (obj != null) {
                return obj.toString().trim();
            }

        } catch (NamingException e) {
            // ignore
        }

        return null;
    }

}
