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
package org.jboss.seam.faces.examples.shortly.ftest;

import static org.jboss.arquillian.ajocado.Ajocado.waitForHttp;
import static org.jboss.arquillian.ajocado.dom.Attribute.HREF;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.xp;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.locator.XPathLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * A functional test for the short-ly example
 *
 * @author Marek Schmidt
 */
@RunWith(Arquillian.class)
public class ShortlyTest {

    protected final XPathLocator URL_TEXT = xp("//input[contains(@name,':url')]");
    protected final XPathLocator NAME_TEXT = xp("//input[contains(@name,':name')]");
    protected final XPathLocator CREATE_BUTTON = xp("//input[contains(@value,'Create')]");
    protected final XPathLocator DELETEALL_BUTTON = xp("//input[contains(@value,'deleteAll')]");

    protected final XPathLocator ROOT_LINK = xp("//a[text()=\"root\"]");
    protected final AttributeLocator<XPathLocator> ROOT_LINK_HREF = ROOT_LINK.getAttribute(HREF);

    protected final XPathLocator BAR_LINK = xp("//a[text()=\"bar\"]");
    public static final String ARCHIVE_NAME = "faces-shortly.war";
    public static final String BUILD_DIRECTORY = "target";
    
    @ArquillianResource
    URL contextPath;
       
    @Drone
    AjaxSelenium selenium;
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(ZipImporter.class, ARCHIVE_NAME).importFrom(new File(BUILD_DIRECTORY + '/' + ARCHIVE_NAME))
                .as(WebArchive.class);
    }

    @Before
    public void openStartUrl() throws MalformedURLException {
        selenium.setSpeed(300);
        contextPath = new URL(contextPath.toString().replaceAll("127.0.0.1", "localhost"));
        selenium.open(new URL(contextPath.toString()));
    }

    @Test
    public void testCreate() throws MalformedURLException, URISyntaxException {
        // deleteAll button is not displayed if there are no links
        assertEquals(selenium.isElementPresent(DELETEALL_BUTTON), false);

        // We can only test pages on the same domain, the only interesting page we can be quite sure to exist on the same domain
        // is the context root
        selenium.type(URL_TEXT, contextPath.toString());
        selenium.type(NAME_TEXT, "root");
        waitForHttp(selenium).click(CREATE_BUTTON);
        assertEquals(selenium.getAttribute(ROOT_LINK_HREF), "/faces-shortly/root");
        assertEquals(selenium.isTextPresent("Created link root"), true);
        waitForHttp(selenium).click(ROOT_LINK);
        assertEquals(selenium.getLocation().toString(), contextPath.toString());
        testDeleteAll();
    }
    
    
    public void testDeleteAll() {
        waitForHttp(selenium).click(DELETEALL_BUTTON);
        assertEquals(selenium.isTextPresent("All links deleted"), true);
        assertEquals(selenium.isElementPresent(DELETEALL_BUTTON), false);
        assertEquals(selenium.isElementPresent(ROOT_LINK), false);
    }

    @Test
    public void testValidation() {
        selenium.type(URL_TEXT, "foo");
        selenium.type(NAME_TEXT, "bar");
        waitForHttp(selenium).click(CREATE_BUTTON);
        assertEquals(selenium.isTextPresent("Must be a valid web address"), true);
        assertEquals(selenium.isElementPresent(BAR_LINK), false);
    }
}
