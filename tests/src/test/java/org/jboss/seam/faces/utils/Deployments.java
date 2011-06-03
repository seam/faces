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
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.AuthMethodType;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.jboss.shrinkwrap.descriptor.spi.NodeProvider;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * Deployments
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class Deployments {
    // property surefire sys prop setting
    public static final boolean IS_JETTY = (System.getProperty("jetty-embedded") != null);
    public static final boolean IS_TOMCAT = (System.getProperty("tomcat-embedded") != null);

    public static WebArchive createCDIDeployment() {
        WebArchive war = createBaseDeployment();
        war.setWebXML(new StringAsset(createCDIWebXML().exportAsString()));
        System.out.println(" 4 == " + war.toString(true));
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(" 5 == " + war.toString(true));
        if (IS_TOMCAT) {
            war.addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
                    .artifacts("org.jboss.weld.servlet:weld-servlet:1.1.0.Final").resolveAsFiles());
            war.addAsManifestResource(new File("src/main/tomcat/cdi-context.xml"), "context.xml");
        }
        if (IS_JETTY) {
            war.addAsWebInfResource(new File("src/main/jetty/cdi-jetty-env.xml"), "jetty-env.xml");
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
                new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml");
        war.addAsWebResource(new File("src/main/webapp", "index.xhtml"))
                .addAsWebResource(new File("src/main/webapp", "finalgreeting.xhtml"))
                .addAsWebResource(new File("src/main/webapp", "secured-page.xhtml"))
                .addAsWebResource(new File("src/main/webapp", "NestedNamingContainers.xhtml"))
                .addAsWebResource(new File("src/main/webapp", "indexWithExtraComponents.xhtml"))
                .addAsWebResource(new File("src/main/webapp", "marathons.xhtml"))
                .addAsWebResource(new File("src/main/webapp", "marathons_datatable.xhtml"));
        appendForEmbedded(war);
        return war;
    }

    private static void appendForEmbedded(WebArchive war) {
        if (IS_JETTY || IS_TOMCAT) {
            war.addAsLibraries(DependencyResolvers
                    .use(MavenDependencyResolver.class)
                    .artifacts("com.sun.faces:jsf-api:2.0.4-b03", "com.sun.faces:jsf-impl:2.0.4-b03",
                            "javax.annotation:jsr250-api:1.0", "javax.servlet:jstl:1.2").resolveAsFiles());
        }
        if (IS_JETTY) {
            war.addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
                    .artifacts("org.glassfish.web:el-impl:2.2").resolveAsFiles());
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

        if (!(IS_JETTY || IS_TOMCAT)) {
            // SHRINKDESC-48 desc.securityConstraint("Basic Authentication for the Admin")
            desc.securityConstraint().webResourceCollection("Authenticated").urlPatterns("/secured-page.faces")
                    .authConstraint("hellotestadmin").loginConfig(AuthMethodType.BASIC, "Authenticated")
                    .securityRole("hellotestadmin");
        }
        if (IS_JETTY) {
            desc.listener("org.jboss.weld.environment.servlet.Listener");
            desc.listener("com.sun.faces.config.ConfigureListener");
        }
        if (IS_TOMCAT) {
            desc.version("2.5");
        }
    }
}
