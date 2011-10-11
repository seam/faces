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

import java.net.URL;

import org.jboss.seam.solder.util.Sortable;

/**
 * 
 * SPI for finding the location of <code>web.xml</code>.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public interface WebXmlLocator extends Sortable {

    /**
     * Returns the guessed location of <code>web.xml</code>.
     * 
     * @param classLoader The classloader to use for resource lookups
     * @return The location of <code>web.xml</code> or <code>null</code> if the location could not be identified
     */
    URL getWebXmlLocation(ClassLoader classLoader);

}
