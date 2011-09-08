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
package org.jboss.seam.faces.test.view.config;

import java.lang.annotation.Annotation;
import java.util.List;

import junit.framework.Assert;
import org.jboss.seam.faces.test.view.config.annotation.Icon;
import org.jboss.seam.faces.test.view.config.annotation.IconLiteral;
import org.jboss.seam.faces.test.view.config.annotation.QualifiedIcon;
import org.jboss.seam.faces.test.view.config.annotation.QualifiedIconLiteral;
import org.jboss.seam.faces.test.view.config.annotation.QualifiedUrl;
import org.jboss.seam.faces.test.view.config.annotation.QualifiedUrlLiteral;
import org.jboss.seam.faces.test.view.config.annotation.TestQualifier;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Stuart Douglas
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */

public class ViewConfigStoreTest {
    ViewConfigStore store;

    @Before
    public void setup() {
        store = new ViewConfigStoreImpl();
        store.addAnnotationData("/*", new IconLiteral("default.gif"));
        store.addAnnotationData("/sad/*", new IconLiteral("sad.gif"));
        store.addAnnotationData("/happy/*", new IconLiteral("happy.gif"));
        store.addAnnotationData("/happy/done.xhtml", new IconLiteral("finished.gif"));
        store.addAnnotationData("/qualified/yes.xhtml", new QualifiedUrlLiteral("http://example.com"));
        store.addAnnotationData("/qualified/yes.xhtml", new QualifiedIconLiteral("qualified.gif"));
    }

    @Test
    public void testViewConfigStore() {
        Icon data;
        data = store.getAnnotationData("/happy/done.xhtml", Icon.class);
        Assert.assertEquals("finished.gif", data.value());
        data = store.getAnnotationData("/happy/other.xhtml", Icon.class);
        Assert.assertEquals("happy.gif", data.value());
        data = store.getAnnotationData("/default/news.xhtml", Icon.class);
        Assert.assertEquals("default.gif", data.value());
        QualifiedIcon qualifiedData;
        qualifiedData = store.getAnnotationData("/qualified/yes.xhtml", QualifiedIcon.class);
        Assert.assertEquals("qualified.gif", qualifiedData.value());
        QualifiedUrl qualifiedUrl;
        qualifiedUrl = store.getAnnotationData("/qualified/yes.xhtml", QualifiedUrl.class);
        Assert.assertEquals("http://example.com", qualifiedUrl.value());

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
    public void testViewConfigStoreQualified() {
        List<? extends Annotation> qdlist;
        qdlist = store.getAllQualifierData("/qualified/yes.xhtml", TestQualifier.class);
        Assert.assertEquals(2, qdlist.size());
        Assert.assertEquals("qualified.gif", ((QualifiedIcon) qdlist.get(0)).value());
        Assert.assertEquals("http://example.com", ((QualifiedUrl) qdlist.get(1)).value());
    }
}
