/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.seam.faces.rewrite;

import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import java.lang.annotation.Annotation;
import javax.servlet.ServletContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
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
