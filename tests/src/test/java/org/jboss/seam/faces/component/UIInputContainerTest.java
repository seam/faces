package org.jboss.seam.faces.component;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.utils.Deployments;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UIInputContainerTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = Deployments.createDeployment();
        return war;
    }

    @Test
    public void checkUIInputcontainerState() {
        Assert.assertEquals("", "");
    }

}
