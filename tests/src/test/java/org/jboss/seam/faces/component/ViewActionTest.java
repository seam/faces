package org.jboss.seam.faces.component;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.api.InitialPage;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.seam.faces.utils.Deployments;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

/**
 * UIInputCointainer testcase
 * 
 * @author <a href="http://community.jboss.org/people/bleathem)">Brian Leathem</a>
 * 
 */

@RunWith(Arquillian.class)
public class ViewActionTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = Deployments.createSeamFacesDeployment();
        war.addClass(ViewActionTestBean.class)
            .addAsWebResource(new File("src/test/webapp/ViewAction", "form.xhtml"))
            .addAsWebResource(new File("src/test/webapp/ViewAction", "result.xhtml"));
        return war;
    }

    @Test
    @InitialPage("/form.xhtml")
    public void checkComponentRenderAfterSuccess(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertEquals("/result.xhtml", server.getCurrentViewID());
        Assert.assertTrue(client.getPageAsText().contains("Result page"));
    }
}
