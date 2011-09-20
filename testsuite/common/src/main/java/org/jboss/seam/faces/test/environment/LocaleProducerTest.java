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
package org.jboss.seam.faces.test.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.enterprise.inject.Instance;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.environment.FacesContextProducer;
import org.jboss.seam.faces.environment.LocaleProducer;
import org.jboss.seam.faces.qualifier.Faces;
import org.jboss.seam.international.locale.DefaultLocale;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verify that the LocaleProducer produces the same Locales as returned by {@link Application#getDefaultLocale()} and the CDI
 * producer method.
 * 
 * @author George Gastaldi
 */
@RunWith(Arquillian.class)
public class LocaleProducerTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class).addClass(FacesContextProducer.class).addClass(LocaleProducer.class)
                .addAsManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
    }

    @Inject
    @Faces
    @DefaultLocale
    Instance<Locale> defaultLocaleInstance;

    @Inject
    @Faces
    Instance<List<Locale>> supportedLocalesInstance;

    @Test
    public void testReturnsSupportedLocales() {
        new MockFacesContext().set();
        FacesContext ctx = FacesContext.getCurrentInstance();
        Iterator<Locale> supportedLocales = ctx.getApplication().getSupportedLocales();
        List<Locale> supportedLocales2 = new LocaleProducer().getSupportedLocales(ctx);
        assertArrayEquals(toArray(supportedLocales), toArray(supportedLocales2.iterator()));
    }

    @Test
    public void testReturnsDefaultLocale() {
        new MockFacesContext().set();
        FacesContext ctx = FacesContext.getCurrentInstance();
        assertSame(new LocaleProducer().getDefaultFacesLocale(ctx), ctx.getApplication().getDefaultLocale());
    }

    @Test
    public void testProducesDefaultLocaleCurrentFacesContext() {
        new MockFacesContext().set();

        Locale actualDefaultLocale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
        Locale producedDefaultLocale = defaultLocaleInstance.get();

        // verify we have same object through proxy by comparing hash codes
        assertEquals(actualDefaultLocale, producedDefaultLocale);
    }

    private Object[] toArray(Iterator<?> iterator) {
        List<Object> list = new ArrayList<Object>();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        return list.toArray();
    }
}
