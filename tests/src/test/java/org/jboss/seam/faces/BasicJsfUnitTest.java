package org.jboss.seam.faces;

import java.io.File;
import java.io.IOException;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.api.InitialPage;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.seam.faces.component.UIInputContainer;
import org.jboss.seam.faces.component.UIInputContainerTestBean;
import org.jboss.seam.faces.utils.Deployments;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UIInputCointainer testcase
 * 
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 * 
 */

@RunWith(Arquillian.class)
public class BasicJsfUnitTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = Deployments.createSeamFacesDeployment();
        war.addAsWebResource(new File("src/test/webapp/BasicJsfUnit", "form.xhtml"))
            .addAsWebResource(new File("src/test/webapp/BasicJsfUnit", "result.xhtml"));
        return war;
    }



    @Test
    @InitialPage("/form.xhtml")
    public void checkComponentRenderAfterSuccess(JSFServerSession server, JSFClientSession client) throws IOException {
        if (server == null)
            throw new IllegalStateException("JSFServerSession is null");
        if (server == null)
            throw new IllegalStateException("JSFClientSession is null");

        Assert.assertEquals("/form.xhtml", server.getCurrentViewID());

        client.click("form:submit");

        // also related to https://issues.jboss.org/browse/SEAMFACES-47
        // Assert.assertTrue(!isInputContainerInvalid(server, "age"));
        // Assert.assertTrue(!isInputContainerInvalid(server, "name"));

        Assert.assertEquals("/result.xhtml", server.getCurrentViewID());
        Assert.assertTrue(client.getPageAsText().contains("Result page"));
    }
}
