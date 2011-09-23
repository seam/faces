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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.jboss.seam.logging.Logger;

/**
 * This implementation of {@link WebXmlLocator} will try to identify the location of <code>web.xml</code> by examining the URLs
 * the classloader uses for loading classes. This will only work if the classloader is a {@link URLClassLoader}.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class URLClassLoaderWebXmlLocator implements WebXmlLocator {

    private final Logger log = Logger.getLogger(URLClassLoaderWebXmlLocator.class);

    @Override
    public int getPrecedence() {
        return 100;
    }

    @Override
    public URL getWebXmlLocation(ClassLoader classLoader) {

        // this class only works for URLClassLoaders
        if (classLoader instanceof URLClassLoader) {

            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;

            // get the URLs of the classloader
            for (URL classLoaderUrl : urlClassLoader.getURLs()) {

                // try this URL to locate the web.xml
                URL possibleWebXmlLocation = processClassLoaderSearchPath(classLoaderUrl);

                // we will use the first result we get
                if (possibleWebXmlLocation != null) {
                    return possibleWebXmlLocation;
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

        return null;

    }

    /**
     * Try to locate the web.xml using the supplied classloader URL.
     * 
     * @param classPathUrl The {@link URL} the classloader uses to look for classes
     * @return The guessed location of web.xml or <code>null</code> if it could not be determined
     */
    private URL processClassLoaderSearchPath(URL classPathUrl) {

        // we use string comparisons here
        String location = classPathUrl.toString();

        // ignore JAR files as they don't help us
        if (location.endsWith(".jar")) {
            return null;
        }

        if (log.isTraceEnabled()) {
            log.trace("Found URL of directory: " + location);
        }

        // The "classes" directory could help us to find web.xml
        if (location.endsWith("/classes/")) {

            // should always work as the URL is built using an existing URL
            try {
                return new URL(location.replaceAll("classes/$", "web.xml"));
            } catch (MalformedURLException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Failed to create URL instance!", e);
                }
            }

        }
        return null;

    }

}
