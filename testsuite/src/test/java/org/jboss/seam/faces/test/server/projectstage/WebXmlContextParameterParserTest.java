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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.seam.faces.projectstage.WebXmlContextParameterParser;
import org.junit.Test;

/**
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class WebXmlContextParameterParserTest {

    @Test
    public void testParseWebXml() throws IOException {

        // open the web.xml
        InputStream webXmlStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("org/jboss/seam/faces/test/server/projectstage/parser-test-web.xml");
        assertNotNull("Cannot find web.xml for test", webXmlStream);

        // parse it
        WebXmlContextParameterParser parser = new WebXmlContextParameterParser();
        parser.parse(webXmlStream);

        // validate result
        assertEquals(null, parser.getContextParameter("does not exist"));
        assertEquals("23", parser.getContextParameter("org.example.SOME_PARAMETER"));
        assertEquals("Development", parser.getContextParameter("javax.faces.PROJECT_STAGE"));

    }

}
