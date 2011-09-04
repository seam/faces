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
import java.net.URLClassLoader;

import javax.faces.application.ProjectStage;
import javax.servlet.ServletContext;

import org.jboss.seam.logging.Logger;

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
        String projectStageName = getProjectStageFromWebXml();

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
     * Tries to get the name of the project stage from the web.xml. This method will return <code>null</code> if the project
     * stage could not be determined.
     * 
     * @return name of the project stage or <code>null</code>
     */
    private String getProjectStageFromWebXml() {

        // use the context class loader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // we currently support only URLClassLoaders
        if (classLoader instanceof URLClassLoader) {

            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;

            // get the search path of the class loader
            for (URL searchPathUrl : urlClassLoader.getURLs()) {

                // try this URL to locate the web.xml
                String projectStage = processClassLoaderSearchPath(searchPathUrl);

                // abort if we found the project stage as it makes no sense to search further
                if (projectStage != null) {
                    return projectStage;
                }

            }

        }

        // log class loader type in all other cases
        else {
            if (log.isTraceEnabled()) {
                log.trace("Context class loader is not an URLClassLoader but: "
                        + (classLoader != null ? classLoader.getClass().getName() : "null"));
            }
        }

        // we don't know
        return null;

    }

    /**
     * Try to get the project stage from the supplied classloader search path URL.
     * 
     * @param classPathUrl The {@link URL} the classloader uses to search for classes
     * @return The project stage or <code>null</code> if it could not be determined
     */
    private String processClassLoaderSearchPath(URL classPathUrl) {

        // the base URL for the search
        String baseUrl = classPathUrl.toString();

        // ignore JAR files as they don't help us
        if (baseUrl.endsWith(".jar")) {
            return null;
        }

        if (log.isTraceEnabled()) {
            log.trace("Found classloader search path: " + baseUrl);
        }

        // The "classes" directory could help us to find web.xml
        if (baseUrl.endsWith("/classes/")) {

            // try to open web.xml using a path relative to the classes folder
            String projectStage = parseWebXml(baseUrl + "../web.xml");

            // have we found the project stage?
            if (projectStage != null) {
                return projectStage;
            }

        }

        return null;

    }

    /**
     * Tries to parse the project stage from a guessed location of the web.xml. The method will return <code>null</code> if the
     * web.xml does not exist, cannot be opened, a parsing error occurred or if the web.xml doesn't contain a context parameter
     * specifying the project stage.
     * 
     * @param location URL of the web.xml
     * @return project stage or <code>null</code>
     */
    private String parseWebXml(String location) {

        if (log.isTraceEnabled()) {
            log.trace("Processing possible web.xml location: " + location);
        }

        InputStream webXmlStream = null;

        // try to open this guessed location of the web.xml
        try {

            webXmlStream = new URL(location).openStream();

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
