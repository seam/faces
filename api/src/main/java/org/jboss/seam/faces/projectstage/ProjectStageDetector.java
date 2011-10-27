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

import org.jboss.solder.util.Sortable;

/**
 * SPI for providing different ways to obtain the current project stage
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public interface ProjectStageDetector extends Sortable {

    /**
     * Obtain the current project stage. Implementations of this method must return 
     * <code>null</code> if they cannot determine the project stage.
     * 
     * @return Current project stage or <code>null</code> if unknown
     */
    ProjectStage getProjectStage();
    
}
