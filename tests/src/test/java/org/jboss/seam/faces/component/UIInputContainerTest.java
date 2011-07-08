package org.jboss.seam.faces.component;

import java.io.IOException;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.api.InitialPage;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.seam.faces.utils.Deployments;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UIInputCointainer testcase
 * 
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 * 
 */

@RunWith(Arquillian.class)
public class UIInputContainerTest {

    /*
     * @Inject private JSFClientSession client;
     * 
     * @Inject private JSFServerSession server;
     */

    @Inject
    UIInputContainer uiInputContainer;

    private static final String AGE_LABEL_CLIENT_ID = "age:label";
    private static final String AGE_INPUT_CLIENT_ID = "age:input";

    private static final String NAME_LABEL_CLIENT_ID = "name:label";
    private static final String NAME_INPUT_CLIENT_ID = "name:input";

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = Deployments.createCDIDeployment();

        war.addClass(UIInputContainerTestBean.class).addAsLibraries(
                DependencyResolvers.use(MavenDependencyResolver.class).artifact(Deployments.SEAM_FACES_JAR)
                        .resolveAs(GenericArchive.class));

        return war;
    }

    @Test
    @InitialPage("/inputcontainerform.xhtml")
    public void componentRenderTest(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertEquals("/inputcontainerform.xhtml", server.getCurrentViewID());

        HtmlOutputLabel ageLabel = (HtmlOutputLabel) server.findComponent(AGE_LABEL_CLIENT_ID);
        HtmlInputText ageInput = (HtmlInputText) server.findComponent(AGE_INPUT_CLIENT_ID);

        HtmlOutputLabel nameLabel = (HtmlOutputLabel) server.findComponent(NAME_LABEL_CLIENT_ID);
        HtmlInputText nameInput = (HtmlInputText) server.findComponent(NAME_INPUT_CLIENT_ID);

        Assert.assertTrue(ageLabel.isRendered() && ageInput.isRendered() && nameLabel.isRendered() && nameInput.isRendered());

        Assert.assertEquals(AGE_INPUT_CLIENT_ID, ageLabel.getFor());
        Assert.assertEquals(NAME_INPUT_CLIENT_ID, nameLabel.getFor());

        System.out.println(client.getPageAsText());

        // I don't get why getLocalValue or getValue does not return the rendered text for the label.
        // Assert.assertEquals("foo bar", ageLabel.getLocalValue()); // testing https://issues.jboss.org/browse/SEAMFACES-133
        // Assert.assertEquals("name", nameLabel.getLocalValue());

    }

    @Test
    @InitialPage("/inputcontainerform.xhtml")
    public void checkComponentRenderAfterSuccess(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertEquals("/inputcontainerform.xhtml", server.getCurrentViewID());

        client.setValue(AGE_INPUT_CLIENT_ID, "100");
        client.setValue(NAME_INPUT_CLIENT_ID, "jose_freitas");
        client.click("submitInputContainer");

        // also related to https://issues.jboss.org/browse/SEAMFACES-47
        // Assert.assertTrue(!isInputContainerInvalid(server, "age"));
        // Assert.assertTrue(!isInputContainerInvalid(server, "name"));

        Assert.assertTrue(client.getPageAsText().contains("The test succeeded with jose_freitas of 100 years old"));
    }

    @Test
    @InitialPage("/inputcontainerform.xhtml")
    public void ComponentInvalidStateTest(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertEquals("/inputcontainerform.xhtml", server.getCurrentViewID());

        client.setValue(AGE_INPUT_CLIENT_ID, "");
        client.setValue(NAME_INPUT_CLIENT_ID, "jose_freitas");
        client.click("submitInputContainer");

        Assert.assertTrue(server.getFacesMessages(AGE_INPUT_CLIENT_ID).hasNext());
        Assert.assertTrue(isInputContainerInvalid(server, "age"));
        Assert.assertTrue(!isInputContainerInvalid(server, "name"));

        Assert.assertEquals("jose_freitas", server.getComponentValue(NAME_INPUT_CLIENT_ID));

        client.setValue(AGE_INPUT_CLIENT_ID, "100");
        client.setValue(NAME_INPUT_CLIENT_ID, "");
        client.click("submitInputContainer");

        Assert.assertTrue(server.getFacesMessages(NAME_INPUT_CLIENT_ID).hasNext());
        // It keeps invalid state to long https://issues.jboss.org/browse/SEAMFACES-47
        // Assert.assertTrue(!isInputContainerInvalid(server, "age")); uncomment this line when the bug is fixed
        Assert.assertTrue(isInputContainerInvalid(server, "name"));

        Assert.assertEquals(100, server.getComponentValue(AGE_INPUT_CLIENT_ID));

        checkComponentRenderAfterSuccess(server, client);

        // AssertTrue(server.get)
    }

    /**
     * This method analyses the inputcontainer's 'invalid' attribute value
     * 
     * @param server
     * @param inputContainerClientId
     * @return
     */
    private boolean isInputContainerInvalid(JSFServerSession server, String inputContainerClientId) {
        UIInputContainer inputContainer = (UIInputContainer) server.findComponent(inputContainerClientId);
        if (inputContainer.getAttributes().get(inputContainer.getInvalidAttributeName()) != null)
            return "true".equals(inputContainer.getAttributes().get(inputContainer.getInvalidAttributeName()).toString());
        return false;
    }
}
