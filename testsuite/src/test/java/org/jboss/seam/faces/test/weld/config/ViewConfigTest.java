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
package org.jboss.seam.faces.test.weld.config;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.security.RestrictAtPhase;
import org.jboss.seam.faces.test.weld.config.annotation.Icon;
import org.jboss.seam.faces.test.weld.config.annotation.IconLiteral;
import org.jboss.seam.faces.test.weld.config.annotation.QualifiedIcon;
import org.jboss.seam.faces.test.weld.config.annotation.RestrictedAtRestoreView;
import org.jboss.seam.faces.test.weld.config.annotation.ViewConfigEnum;
import org.jboss.seam.faces.view.config.ViewConfigDescriptor;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Stuart Douglas
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@RunWith(Arquillian.class)
public class ViewConfigTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addClass(ViewConfigTest.class)
                .addClass(ViewConfigStoreImpl.class).addPackage(ViewConfigEnum.class.getPackage())
                .addAsManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
        return archive;
    }

    @Inject
    private ViewConfigStore store;

    @Test
    public void testViewConfigStore() {

        store.addAnnotationData("/*", new IconLiteral("default.gif"));
        store.addAnnotationData("/sad/*", new IconLiteral("sad.gif"));
        store.addAnnotationData("/happy/*", new IconLiteral("happy.gif"));
        store.addAnnotationData("/happy/done.xhtml", new IconLiteral("finished.gif"));

        Icon data;
        data = store.getAnnotationData("/happy/done.xhtml", Icon.class);
        Assert.assertEquals("finished.gif", data.value());
        data = store.getAnnotationData("/happy/other.xhtml", Icon.class);
        Assert.assertEquals("happy.gif", data.value());
        data = store.getAnnotationData("/default/news.xhtml", Icon.class);
        Assert.assertEquals("default.gif", data.value());

        List<Icon> dlist;
        dlist = store.getAllAnnotationData("/happy/done.xhtml", Icon.class);
        Assert.assertEquals(3, dlist.size());
        Assert.assertEquals("finished.gif", dlist.get(0).value());
        Assert.assertEquals("happy.gif", dlist.get(1).value());
        Assert.assertEquals("default.gif", dlist.get(2).value());
        dlist = store.getAllAnnotationData("/happy/other.xhtml", Icon.class);
        Assert.assertEquals(2, dlist.size());
        Assert.assertEquals("happy.gif", dlist.get(0).value());
        Assert.assertEquals("default.gif", dlist.get(1).value());
        dlist = store.getAllAnnotationData("/default/news.xhtml", Icon.class);
        Assert.assertEquals(1, dlist.size());
        Assert.assertEquals("default.gif", dlist.get(0).value());

    }

    @Test
    public void testViewConfigDescriptor() {
        
        Map<String,ViewConfigDescriptor> descriptorMap = new HashMap<String, ViewConfigDescriptor>();
        for (ViewConfigDescriptor descriptor : store.getViewConfigDescriptors()) {
            Assert.assertFalse("duplicated viewId "+descriptor.getViewId(), descriptorMap.containsKey(descriptor.getViewId()));
            descriptorMap.put(descriptor.getViewId(), descriptor);
        }
        
        String viewId = "/happy/done.xhtml";
        ViewConfigDescriptor descriptor = descriptorMap.get(viewId);
        Assert.assertEquals(viewId, descriptor.getViewId());
        Assert.assertEquals(ViewConfigEnum.Pages.HAPPY_DONE, descriptor.getValues().get(0));
        Icon data = descriptor.getMetaData(Icon.class);
        Assert.assertEquals("finished.gif", data.value());
        
        viewId = "/happy/*";
        descriptor = descriptorMap.get(viewId);
        Assert.assertEquals(viewId, descriptor.getViewId());
        Assert.assertEquals(ViewConfigEnum.Pages.HAPPY, descriptor.getValues().get(0));
        data = descriptor.getMetaData(Icon.class);
        Assert.assertEquals("happy.gif", data.value());
        
        viewId = "/sad/*";
        descriptor = descriptorMap.get(viewId);
        Assert.assertEquals(viewId, descriptor.getViewId());
        Assert.assertEquals(ViewConfigEnum.Pages.SAD, descriptor.getValues().get(0));
        data = descriptor.getMetaData(Icon.class);
        Assert.assertEquals("sad.gif", data.value());
        
        viewId = "/*";
        descriptor = descriptorMap.get(viewId);
        Assert.assertEquals(viewId, descriptor.getViewId());
        Assert.assertEquals(ViewConfigEnum.Pages.DEFAULT, descriptor.getValues().get(0));
        data = descriptor.getMetaData(Icon.class);
        Assert.assertEquals("default.gif", data.value());

        QualifiedIcon qualifiedData;
        descriptor = descriptorMap.get("/qualified/*");
        qualifiedData = descriptor.getMetaData(QualifiedIcon.class);
        Assert.assertEquals(ViewConfigEnum.Pages.QUALIFIED, descriptor.getValues().get(0));
        Assert.assertEquals("qualified.gif", qualifiedData.value());

        descriptor = descriptorMap.get("/qualified/yes.xhtml");
        Assert.assertEquals(ViewConfigEnum.Pages.QUALIFIED_YES, descriptor.getValues().get(0));
        List<? extends Annotation> annotations = descriptor.getAllQualifierData(RestrictAtPhase.class);
        Assert.assertEquals(1, annotations.size());
        Assert.assertTrue(RestrictedAtRestoreView.class.isAssignableFrom(annotations.get(0).getClass()));

        Assert.assertEquals(6, descriptorMap.size());
    }
}
