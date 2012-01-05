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
package org.jboss.seam.faces.test.server.projectstage;

import org.jboss.seam.faces.test.BaseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class ProjectStageBaseArchive {

    public static WebArchive getBaseArchive() {
        return BaseArchive.baseWar("test.war", true)
                .addAsLibraries(BaseArchive.retrieveLibs(
                        "org.jboss.seam.faces:seam-faces", 
                        "org.jboss.seam.faces:seam-faces-api",
                        "org.jboss.seam.security:seam-security", 
                        "joda-time:joda-time:2.0",
                        "com.ocpsoft:prettyfaces-jsf2:3.3.2"));
    }

}
