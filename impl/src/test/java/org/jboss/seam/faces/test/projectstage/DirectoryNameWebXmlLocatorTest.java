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
package org.jboss.seam.faces.test.projectstage;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.net.URL;

import org.jboss.seam.faces.projectstage.DirectoryNameWebXmlLocator;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link DirectoryNameWebXmlLocator}. Please note that this test won't use the 'vfs' protocol because
 * this won't work in a simple unit test outside of a container. We will simply use the 'file' protocol instead, which
 * will also work fine to verify the results.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class DirectoryNameWebXmlLocatorTest {

    @Test
    public void testResourceFromWebLibDirectoryAS7() throws Exception {

        final String resourceName = DirectoryNameWebXmlLocator.class.getName().replace('.', '/') + ".class";
        final URL resourceUrl = new URL("file:/jboss-home/server/default/deploy/seam-faces-tests.war/WEB-INF/lib/seam-faces-3.1.0-SNAPSHOT.jar/" + resourceName);

        ClassLoader classLoader = Mockito.mock(ClassLoader.class);
        Mockito.when(classLoader.getResource(resourceName)).thenReturn(resourceUrl);

        DirectoryNameWebXmlLocator locator = new DirectoryNameWebXmlLocator();
        URL resultUrl = locator.getWebXmlLocation(classLoader);

        assertNotNull(resultUrl);
        assertEquals("file:/jboss-home/server/default/deploy/seam-faces-tests.war/WEB-INF/web.xml", resultUrl.toString());

    }

    @Test
    public void testResourceFromWebLibDirectoryAS6() throws Exception {

        final String resourceName = DirectoryNameWebXmlLocator.class.getName().replace('.', '/') + ".class";
        final URL resourceUrl = new URL("file:/content/seam-faces-tests.war/WEB-INF/lib/seam-faces-3.1.0-SNAPSHOT.jar/" + resourceName);

        ClassLoader classLoader = Mockito.mock(ClassLoader.class);
        Mockito.when(classLoader.getResource(resourceName)).thenReturn(resourceUrl);

        DirectoryNameWebXmlLocator locator = new DirectoryNameWebXmlLocator();
        URL resultUrl = locator.getWebXmlLocation(classLoader);

        assertNotNull(resultUrl);
        assertEquals("file:/content/seam-faces-tests.war/WEB-INF/web.xml", resultUrl.toString());

    }
    
    @Test
    public void testResourceFromWebLibDirectoryTomcat7() throws Exception {
        
        final String resourceName = DirectoryNameWebXmlLocator.class.getName().replace('.', '/') + ".class";
        final URL resourceUrl = new URL("jar:file:/tomcat-home/webapps/seam-faces-tests/WEB-INF/lib/seam-faces-3.1.0-SNAPSHOT.jar!/" + resourceName);
        
        ClassLoader classLoader = Mockito.mock(ClassLoader.class);
        Mockito.when(classLoader.getResource(resourceName)).thenReturn(resourceUrl);
        
        DirectoryNameWebXmlLocator locator = new DirectoryNameWebXmlLocator();
        URL resultUrl = locator.getWebXmlLocation(classLoader);
        
        assertNotNull(resultUrl);
        assertEquals("file:/tomcat-home/webapps/seam-faces-tests/WEB-INF/web.xml", resultUrl.toString());
        
    }

}
