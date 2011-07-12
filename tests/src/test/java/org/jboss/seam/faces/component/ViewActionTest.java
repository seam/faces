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
            .addAsWebResource(new File("src/test/webapp/ViewAction", "goto_result.xhtml"))
            .addAsWebResource(new File("src/test/webapp/ViewAction", "result.xhtml"))
            .addAsWebResource(new File("src/test/webapp/ViewAction", "load_data.xhtml"));
        return war;
    }

    @Test
    @InitialPage("/goto_result.xhtml")
    public void checkNavigation(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertEquals("/result.xhtml", server.getCurrentViewID());
        Assert.assertTrue(client.getPageAsText().contains("Result page"));
    }

    @Test
    @InitialPage("/load_data.xhtml")
    public void checkDataLoad(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertEquals("/load_data.xhtml", server.getCurrentViewID());
        Assert.assertTrue(client.getPageAsText().contains("Data Loaded"));
    }
}
