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
package org.jboss.seam.faces.rewrite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author bleathem
 */
public class RewriteConfigurationTest {

    public RewriteConfigurationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of buildViewUrl method, of class RewriteConfiguration.
     */
    @Test
    public void testBuildViewUrl() {
        System.out.println("buildViewUrl");
        RewriteConfiguration instance = new RewriteConfiguration();
        assertEquals("/faces/index.xhtml", instance.buildViewUrl("index.xhtml", "/faces/*"));
        assertEquals("/index.jsf", instance.buildViewUrl("index.xhtml", "*.jsf"));
        assertEquals("/path/to/some/file.jsf", instance.buildViewUrl("/path/to/some/file.xhtml", "*.jsf"));
        assertEquals("/index.jsf", instance.buildViewUrl("index", "*.jsf"));
    }
}
