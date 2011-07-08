/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
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
package org.jboss.seam.faces.utils;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.Node;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.jboss.shrinkwrap.descriptor.spi.NodeProvider;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * Deployments class has the responsability to manage the creation of the base archives to be deployed for testing purpose.
 * 
 * this class was adapted from arquillian-hellojsf example
 * 
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 */
public class Deployments {

    public static final boolean IS_JETTY = (System.getProperty("jetty-embedded") != null);
    public static final boolean IS_TOMCAT = (System.getProperty("tomcat-embedded") != null);

    public static final String SEAM_FACES_JAR = "org.jboss.seam.faces:seam-faces:3.1.0-SNAPSHOT";
    public static final String WELD_IMPL_JAR = "org.jboss.weld.servlet:weld-servlet:1.1.0.Final";
    public static final String JSF_API_JAR = "com.sun.faces:jsf-api:2.0.4-b03";
    public static final String JSF_IMPL_JAR = "com.sun.faces:jsf-impl:2.0.4-b03";
    public static final String JSR_250_API_JAR = "javax.annotation:jsr250-api:1.0";
    public static final String SERVLET_JSTL_JAR = "javax.servlet:jstl:1.2";
    public static final String EL_IMPL_JAR = "org.glassfish.web:el-impl:2.2";

    public static WebArchive createCDIDeployment() {
        WebArchive war = createBaseDeployment();
        war.setWebXML(new StringAsset(createCDIWebXML().exportAsString()));
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        if (IS_TOMCAT) {
            war.addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class).artifacts(WELD_IMPL_JAR).resolveAsFiles());
            war.addAsManifestResource(new File("src/test/resources/tomcat/cdi-context.xml"), "context.xml");
        }
        if (IS_JETTY) {
            war.addAsWebInfResource(new File("src/test/resources/jetty/cdi-jetty-env.xml"), "jetty-env.xml");
        }
        return war;
    }

    public static WebArchive createDeployment() {
        WebArchive war = createBaseDeployment();
        war.setWebXML(new StringAsset(createWebXML().exportAsString()));

        return war;
    }

    private static WebArchive createBaseDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).addAsWebInfResource(
                new File("src/test/webapp/WEB-INF/faces-config.xml"), "faces-config.xml");
        war.addAsWebResource(new File("src/test/webapp", "index.xhtml")).addAsWebResource(
                new File("src/test/webapp", "inputcontainerform.xhtml"));
        appendForEmbedded(war);

        return war;
    }

    private static void appendForEmbedded(WebArchive war) {
        if (IS_JETTY || IS_TOMCAT) {
            war.addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
                    .artifacts(JSF_API_JAR, JSF_IMPL_JAR, JSR_250_API_JAR, SERVLET_JSTL_JAR).resolveAsFiles());
        }
        if (IS_JETTY) {
            war.addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class).artifacts(EL_IMPL_JAR).resolveAsFiles());
        }
    }

    public static WebAppDescriptor createCDIWebXML() {
        WebAppDescriptor desc = Descriptors.create(WebAppDescriptor.class);

        if (IS_JETTY || IS_TOMCAT) {
            if (IS_TOMCAT) // jetty is added by default. It finds weld-servlet on appCl and insists on loading it.
            {
                desc.listener("org.jboss.weld.environment.servlet.Listener");
            }

            Node rootNode = ((NodeProvider) desc).getRootNode();
            rootNode.getOrCreate("/web-app/resource-env-ref").create("resource-env-ref-name").text("BeanManager").parent()
                    .create("resource-env-ref-type").text("javax.enterprise.inject.spi.BeanManager");
        }
        appendBaseWebXML(desc);
        return desc;
    }

    public static WebAppDescriptor createWebXML() {
        WebAppDescriptor desc = Descriptors.create(WebAppDescriptor.class);
        appendBaseWebXML(desc);
        return desc;
    }

    private static void appendBaseWebXML(WebAppDescriptor desc) {
        desc.displayName("Seam Faces TestCase")
                .contextParam("javax.faces.CONFIG_FILES", "/WEB-INF/local-module-faces-config.xml").welcomeFile("index.xhtml")
                .servlet("javax.faces.webapp.FacesServlet", "*.xhtml").loadOnStartup(1);

        if (IS_JETTY) {
            desc.listener("org.jboss.weld.environment.servlet.Listener");
            desc.listener("com.sun.faces.config.ConfigureListener");
        }
        if (IS_TOMCAT) {
            desc.version("2.5");
        }
    }
}
