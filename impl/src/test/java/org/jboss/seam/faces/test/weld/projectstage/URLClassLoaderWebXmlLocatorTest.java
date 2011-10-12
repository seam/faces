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
package org.jboss.seam.faces.test.weld.projectstage;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.jboss.seam.faces.projectstage.URLClassLoaderWebXmlLocator;
import org.junit.Test;
import org.mockito.Mockito;

public class URLClassLoaderWebXmlLocatorTest {

    @Test
    public void testNotAnUrlClassLoader() {

        ClassLoader classLoader = Mockito.mock(ClassLoader.class);

        URL result = new URLClassLoaderWebXmlLocator().getWebXmlLocation(classLoader);

        assertNull(result);

    }

    @Test
    public void testUrlClassLoaderWithoutUrls() {

        URLClassLoader classLoader = Mockito.mock(URLClassLoader.class);
        Mockito.when(classLoader.getURLs()).thenReturn(new URL[0]);

        URL result = new URLClassLoaderWebXmlLocator().getWebXmlLocation(classLoader);

        assertNull(result);

    }

    @Test
    public void testUrlClassLoaderWithoutHelpfulUrls() throws MalformedURLException {

        URLClassLoader classLoader = Mockito.mock(URLClassLoader.class);
        Mockito.when(classLoader.getURLs()).thenReturn(new URL[] { new URL("file:/tmp/test.jar") });

        URL result = new URLClassLoaderWebXmlLocator().getWebXmlLocation(classLoader);

        assertNull(result);

    }

    @Test
    public void testUrlClassLoaderWithClassesDirectory() throws MalformedURLException {

        URLClassLoader classLoader = Mockito.mock(URLClassLoader.class);
        Mockito.when(classLoader.getURLs()).thenReturn(new URL[] {
                new URL("file:/tmp/myapp/WEB-INF/lib/test.jar"),
                new URL("file:/tmp/myapp/WEB-INF/classes/"),
                new URL("file:/tmp/myapp/WEB-INF/lib/test2.jar")
        });

        URL result = new URLClassLoaderWebXmlLocator().getWebXmlLocation(classLoader);

        assertNotNull(result);
        assertEquals("file:/tmp/myapp/WEB-INF/web.xml", result.toString());

    }

    @Test
    public void testUrlClassLoaderWithMavenJettyPlugin() throws MalformedURLException {

        URLClassLoader classLoader = Mockito.mock(URLClassLoader.class);
        Mockito.when(classLoader.getURLs()).thenReturn(new URL[] {
                new URL("file:/home/user/.m2/repository/group/artifact/1.0/artifact-1.0.jar"),
                new URL("file:/somewhere/myapp/target/classes/"),
                new URL("file:/home/user/.m2/repository/group/artifact2/1.0/artifact2-1.0.jar"),
        });

        URL result = new URLClassLoaderWebXmlLocator().getWebXmlLocation(classLoader);

        assertNotNull(result);
        assertEquals("file:/somewhere/myapp/src/main/webapp/WEB-INF/web.xml", result.toString());

    }

}
