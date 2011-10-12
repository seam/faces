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
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Simple SAX parser for reading "context-param" entries from web.xml
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class WebXmlContextParameterParser extends DefaultHandler {

    /**
     * Used to maintain a list of elements up to the root of the XML tree
     */
    private final Stack<String> stack = new Stack<String>();

    /**
     * All context parameters parsed from the file
     */
    private final Map<String, String> entries = new HashMap<String, String>();

    /**
     * The current name and value
     */
    private String currentName;
    private String currentValue;

    /**
     * Parses the supplied web.xml {@link InputStream}.
     * 
     * @param stream The stream to parse
     * @throws IOException for any errors during the parsing process
     */
    public final void parse(InputStream stream) throws IOException {

        // all XML exceptions are wrapped by IOExceptions
        try {

            // setup a namespace aware SAXParserFactory
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);

            // parse the input stream
            SAXParser parser = saxParserFactory.newSAXParser();
            parser.parse(stream, this);

        } catch (ParserConfigurationException e) {
            throw new IOException(e);
        } catch (SAXException e) {
            throw new IOException(e);
        }

    }

    /**
     * Returns the value of the supplied context parameter parsed from the web.xml file. Returns <code>null</code> if the parser
     * found no value for this parameter.
     * 
     * @param name The name of the context parameter
     * @return the value or <code>null</code>
     */
    public String getContextParameter(String name) {
        return entries.get(name);
    }

    /*
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        // update element stack
        stack.add(localName);

        // <context-param>
        if (elements("web-app", "context-param")) {
            currentName = null;
            currentValue = null;
        }

    }

    /*
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        // <param-name>
        if (elements("web-app", "context-param", "param-name")) {
            currentName = String.valueOf(ch, start, length).trim();
        }

        // <param-value>
        if (elements("web-app", "context-param", "param-value")) {
            currentValue = String.valueOf(ch, start, length).trim();
        }

    }

    /*
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        // <context-param>
        if (elements("web-app", "context-param")) {
            if (currentName != null && currentValue != null) {
                entries.put(currentName, currentValue);
            }
        }

        // remove the element from the stack
        String removedName = stack.pop();

        // check if the removed name is the expected one
        if (!removedName.equals(localName)) {
            throw new IllegalStateException("Found '" + removedName + "' but expected '" + localName + "' on the stack!");
        }

    }

    /**
     * Checks whether the element stack currently contains exactly the elements supplied by the caller. This method can be used
     * to find the current position in the document. The first argument is always the root element of the document, the second
     * is a child of the root element, and so on. The method uses the local names of the elements only. Namespaces are ignored.
     * 
     * @param name The names of the parent elements
     * @return <code>true</code> if the parent element stack contains exactly these elements
     */
    protected boolean elements(String... name) {
        if (name == null || name.length != stack.size()) {
            return false;
        }
        for (int i = 0; i < name.length; i++) {
            if (!name[i].equals(stack.get(i))) {
                return false;
            }
        }
        return true;
    }

}
