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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>
 * This implementation of {@link WebXmlLocator} will try to identify the location of <code>web.xml</code> by searching for a
 * known resource on the classpath and trying to find the <code>WEB-INF</code> directory in its path name. This will typically
 * work if the known resource in located in a JAR file inside the <code>WEB-INF/lib</code> directory.
 * </p>
 * 
 * <p>
 * Compared to {@link URLClassLoaderWebXmlLocator} this implementation works fine even for containers like JBoss AS6 and AS7.
 * </p>
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class DirectoryNameWebXmlLocator implements WebXmlLocator {

    private final Logger log = Logger.getLogger(DirectoryNameWebXmlLocator.class);

    @Override
    public int getPrecedence() {
        return 50;
    }

    @Override
    public URL getWebXmlLocation(ClassLoader classLoader) {

        // try to load a resource that MUST exist
        String relativeResourceName = this.getClass().getName().replace('.', '/') + ".class";
        URL knownResource = classLoader.getResource(relativeResourceName);

        // we cannot proceed without that resource
        if (knownResource == null) {
            if (log.isDebugEnabled()) {
                log.debug("Could not find resource: " + relativeResourceName);
            }
            return null;
        }

        // we will now work on the absolute path of this URL
        String url = knownResource.toString();

        if (log.isTraceEnabled()) {
            log.trace("Found known resource: " + url);
        }

        // is the resource located inside a JAR file? Remove the jar-specific part.
        if (url.startsWith("jar:") && url.contains("!")) {

            url = url.substring(4, url.lastIndexOf("!"));

            if (log.isTraceEnabled()) {
                log.trace("Location of JAR file containing the resource: " + url);
            }

        }

        // should always work as the URL is built using an existing URL
        try {

            // try to locate the WEB-INF directory
            int i = url.lastIndexOf("/WEB-INF/lib/");
            if (i >= 0) {
                return new URL(url.substring(0, i) + "/WEB-INF/web.xml");
            }

        } catch (MalformedURLException e) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to create URL instance!", e);
            }
        }
        return null;

    }
}
