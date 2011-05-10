package org.jboss.seam.faces.examples.tinyurl.ftest;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.XpathLocator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.*;
import static org.jboss.test.selenium.locator.Attribute.HREF;
import static org.jboss.test.selenium.locator.LocatorFactory.*;
import static org.testng.Assert.assertEquals;

/**
 * A functional test for the short-ly example
 *
 * @author Marek Schmidt
 */
public class ShortlyTest extends AbstractTestCase {

    protected final XpathLocator URL_TEXT = xp("//input[contains(@name,':url')]");
    protected final XpathLocator NAME_TEXT = xp("//input[contains(@name,':name')]");
    protected final XpathLocator CREATE_BUTTON = xp("//input[contains(@value,'Create')]");
    protected final XpathLocator DELETEALL_BUTTON = xp("//input[contains(@value,'deleteAll')]");

    protected final XpathLocator ROOT_LINK = xp("//a[text()=\"root\"]");
    protected final AttributeLocator ROOT_LINK_HREF = ROOT_LINK.getAttribute(HREF);

    protected final XpathLocator BAR_LINK = xp("//a[text()=\"bar\"]");

    @BeforeMethod
    public void openStartUrl() throws MalformedURLException {
        selenium.setSpeed(300);
        selenium.open(new URL(contextPath.toString()));
    }

    @Test
    public void testCreate() throws MalformedURLException {
        // deleteAll button is not displayed if there are no links
        assertEquals(selenium.isElementPresent(DELETEALL_BUTTON), false);

        // We can only test pages on the same domain, the only interesting page we can be quite sure to exist on the same domain
        // is the context root
        selenium.type(URL_TEXT, contextRoot.toString());
        selenium.type(NAME_TEXT, "root");
        waitHttp(selenium).click(CREATE_BUTTON);

        assertEquals(selenium.getAttribute(ROOT_LINK_HREF), "/short.ly/root");

        waitHttp(selenium).click(ROOT_LINK);
        assertEquals(selenium.getLocation().toString(), contextRoot.toString());
    }

    @Test(dependsOnMethods = {"testCreate"})
    public void testDeleteAll() {
        waitHttp(selenium).click(DELETEALL_BUTTON);
        assertEquals(selenium.isElementPresent(DELETEALL_BUTTON), false);
        assertEquals(selenium.isElementPresent(ROOT_LINK), false);
    }

    @Test
    public void testValidation() {
        selenium.type(URL_TEXT, "foo");
        selenium.type(NAME_TEXT, "bar");
        waitHttp(selenium).click(CREATE_BUTTON);
        assertEquals(selenium.isTextPresent("Must be a valid web address"), true);
        assertEquals(selenium.isElementPresent(BAR_LINK), false);
    }
}
