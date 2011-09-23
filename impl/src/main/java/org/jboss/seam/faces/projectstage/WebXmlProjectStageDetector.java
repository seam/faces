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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.ProjectStage;
import javax.servlet.ServletContext;

import org.jboss.seam.logging.Logger;
import org.jboss.seam.solder.util.Sortable;
import org.jboss.seam.solder.util.service.ServiceLoader;

/**
 * Implementation of {@link ProjectStageDetector} that tries to read the project stage from the standard servlet context
 * parameter. As a CDI extension cannot access the {@link ServletContext}, we try to use the context class loader to find
 * web.xml and parse it manually.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class WebXmlProjectStageDetector implements ProjectStageDetector {

    private final Logger log = Logger.getLogger(WebXmlProjectStageDetector.class);

    @Override
    public int getPrecedence() {
        return 75;
    }

    @Override
    public ProjectStage getProjectStage() {

        // try to get the name of the project stage
        String projectStageName = getProjectStageFromLocators();

        // lookup the correct enum for the value
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
     * Tries to get the name of the project stage from web.xml. This method will use the {@link WebXmlLocator} SPI for locating
     * the file.
     * 
     * @return name of the project stage or <code>null</code>
     */
    private String getProjectStageFromLocators() {

        // build sorted list of locator implementations
        List<WebXmlLocator> locators = new ArrayList<WebXmlLocator>();
        for (Iterator<WebXmlLocator> iter = ServiceLoader.load(WebXmlLocator.class).iterator(); iter.hasNext();) {
            locators.add(iter.next());
        }
        Collections.sort(locators, new Sortable.Comparator());

        // prefer the context classloader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }

        // process each locator one by one
        for (WebXmlLocator locator : locators) {

            // execute the SPI implementation
            URL webXmlLocation = locator.getWebXmlLocation(classLoader);

            // try to parse the web.xml if the locator returned a result
            if (webXmlLocation != null) {

                String projectStage = parseWebXml(webXmlLocation);

                // accept the first result
                if (projectStage != null) {
                    return projectStage;
                }

            }

        }

        // not result found
        return null;

    }

    /**
     * Tries to parse the project stage from the supplied web.xml location. The method will return <code>null</code> if the
     * web.xml does not exist, cannot be opened, a parsing error occurred or if the web.xml doesn't contain a context parameter
     * specifying the project stage.
     * 
     * @param location URL of the web.xml
     * @return project stage or <code>null</code>
     */
    private String parseWebXml(URL location) {

        if (log.isTraceEnabled()) {
            log.trace("Processing possible web.xml location: " + location);
        }

        InputStream webXmlStream = null;

        // try to open this guessed location of the web.xml
        try {

            webXmlStream = location.openStream();

        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug("Unable to open web.xml: " + e.getMessage());
            }
        }

        if (webXmlStream != null) {

            try {

                // parse the input stream
                WebXmlContextParameterParser parser = new WebXmlContextParameterParser();
                parser.parse(webXmlStream);

                // return the project stage if the parser found it
                String projectStage = parser.getContextParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME);

                // did we find a project stage?
                if (projectStage != null) {

                    if (log.isDebugEnabled()) {
                        log.debug("Found project stage in web.xml: " + projectStage);
                    }

                    return projectStage;

                }

            } catch (IOException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Parsing of web.xml failed: " + e.getMessage());
                }
            }

        }

        // no result
        return null;

    }

}
