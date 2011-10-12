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

import org.jboss.solder.logging.Logger;
import org.jboss.solder.util.Sortable;
import org.jboss.solder.util.service.ServiceLoader;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.faces.application.ProjectStage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Extension that supports to veto beans that should only be active in certain 
 * JSF project stages.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class ProjectStageExtension implements Extension {

    private transient final Logger log = Logger.getLogger(ProjectStageExtension.class);

    /**
     * The default project stage
     */
    private final static ProjectStage DEFAULT_PROJECT_STAGE = ProjectStage.Production;

    /**
     * Field to store the detected project stage. The field is lazily initialized in
     * {@link #processAnnotatedType(ProcessAnnotatedType)}
     */
    private ProjectStage stage;

    /**
     * Observes {@link ProcessAnnotatedType} events and sends a veto if the type should be 
     * ignored due to the current project stage.
     */
    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {

        // lazily obtain the project stage
        if (stage == null) {
            stage = detectProjectStage();
        }

        // the project stages explicitly specified on the type
        Set<ProjectStage> validProjectStages = getProjectStagesForType(pat.getAnnotatedType());

        // veto the type if the current project stage doesn't match
        if (validProjectStages.size() > 0 && !validProjectStages.contains(stage)) {

            if (log.isDebugEnabled()) {
                log.debug("Preventing class " + pat.getAnnotatedType().getJavaClass().getName()
                        + " from being installed due to the current project stage");
            }

            // veto this type
            pat.veto();

        }

    }

    /**
     * Returns a set of {@link ProjectStage}s for which the supplied {@link AnnotatedType} has been enabled using one or more of
     * the annotations {@link Development}, {@link Production}, {@link SystemTest} and {@link UnitTest}. The method will return
     * an empty set if the type isn't annotated with any of the annotations.
     * 
     * @param type The {@link AnnotatedType} to process
     * @return The list of project stages
     */
    private <T> Set<ProjectStage> getProjectStagesForType(AnnotatedType<T> type) {

        Set<ProjectStage> stages = new HashSet<ProjectStage>();

        if (type.getAnnotation(Development.class) != null) {
            stages.add(ProjectStage.Development);
        }
        if (type.getAnnotation(Production.class) != null) {
            stages.add(ProjectStage.Production);
        }
        if (type.getAnnotation(SystemTest.class) != null) {
            stages.add(ProjectStage.SystemTest);
        }
        if (type.getAnnotation(UnitTest.class) != null) {
            stages.add(ProjectStage.UnitTest);
        }

        return stages;

    }

    /**
     * Method to detect the current project stage. The method will use the {@link ProjectStageDetector} SPI to obtain the
     * project stage. The fall back value is {@link ProjectStage#Development}.
     */
    private ProjectStage detectProjectStage() {

        // build sorted list of detector implementations
        List<ProjectStageDetector> detectors = new ArrayList<ProjectStageDetector>();
        for (Iterator<ProjectStageDetector> iter = ServiceLoader.load(ProjectStageDetector.class).iterator(); iter.hasNext();) {
            detectors.add(iter.next());
        }
        Collections.sort(detectors, new Sortable.Comparator());

        // try all detectors
        for(ProjectStageDetector detector : detectors) {

            // lets the detector do its job
            ProjectStage stage = detector.getProjectStage();

            if (log.isTraceEnabled()) {
                log.trace("Detector " + detector.getClass().getName() + " returned: " + stage);
            }

            if (stage != null) {
                log.info("Detected project stage: " + stage);
                return stage;
            }

        }

        if (log.isDebugEnabled()) {
            log.debug("No result from detectors! Using default project stage: " + DEFAULT_PROJECT_STAGE);
        }

        return DEFAULT_PROJECT_STAGE;

    }

}
