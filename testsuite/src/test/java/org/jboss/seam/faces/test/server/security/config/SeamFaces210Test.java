/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.faces.test.server.security.config;

import javax.enterprise.inject.spi.Extension;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.security.RestrictAtPhase;
import org.jboss.seam.faces.test.BaseArchive;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewConfigExtension;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://community.jboss.org/people/LightGuard">Jason Porter</a>
 */
@RunWith(Arquillian.class)
public class SeamFaces210Test {
    @Deployment(name = "SeamFaces210")
    @ShouldThrowException(Exception.class)
    public static Archive<?> createTestArchive() {
        final WebArchive testArchive = BaseArchive.baseWar("seamfaces_210.war", true);

        testArchive.addAsLibraries(BaseArchive.retrieveLibs("org.jboss.solder:solder-api", "org.jboss.solder:solder-impl",
                "org.jboss.solder:solder-logging"));

        testArchive.addPackages(true, ViewConfig.class.getPackage(), ViewConfigStoreImpl.class.getPackage(), ViewConfigEnum.class.getPackage())
                .addClasses(SecurityBindingType.class, RestrictAtPhase.class, PhaseIdType.class)
                .addAsServiceProvider(Extension.class, ViewConfigExtension.class);

        return testArchive;
    }

    @Test
    public void emptyMethodToTestDeploymentException() {}
}
