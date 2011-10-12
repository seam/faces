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

/**
 * Implementation of {@link ProjectStageDetector} that allows to set the project stage for Seam Faces using the system property
 * <code>org.jboss.seam.faces.PROJECT_STAGE</code>
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class SystemPropertyProjectStageDetector implements ProjectStageDetector {

    private final static String SYSTEM_PROPERTY_NAME = "org.jboss.seam.faces.PROJECT_STAGE";

    @Override
    public int getPrecedence() {
        return 100;
    }
    
    @Override
    public ProjectStage getProjectStage() {

        // try to read the system property
        String projectStageName = System.getProperty(SYSTEM_PROPERTY_NAME);

        // lookup the correct enum for the value we got from the system property
        if (projectStageName != null && projectStageName.trim().length() > 0) {
            for (ProjectStage stage : ProjectStage.values()) {
                if (stage.name().equalsIgnoreCase(projectStageName.trim())) {
                    return stage;
                }
            }
        }

        // no result
        return null;

    }

}
