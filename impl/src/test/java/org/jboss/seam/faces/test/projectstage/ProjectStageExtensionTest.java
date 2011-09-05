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
package org.jboss.seam.faces.test.projectstage;

import static junit.framework.Assert.assertEquals;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This tests checks if the ProjectStageExtensions works correctly if no project stage can be detected. In this case the
 * extension assumes the Production stage.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
@RunWith(Arquillian.class)
public class ProjectStageExtensionTest {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(DevelopmentStageBean.class, ProductionStageBean.class, NoStageRestrictionBean.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }

    @Inject
    private BeanManager beanManager;

    @Test
    public void testActivatedBeansInDefaultProjectStage() {

        // the development bean has been vetoed
        assertEquals(0, beanManager.getBeans(DevelopmentStageBean.class).size());

        // the production bean is active
        assertEquals(1, beanManager.getBeans(ProductionStageBean.class).size());

        // no annotations on this type, so it is activated
        assertEquals(1, beanManager.getBeans(NoStageRestrictionBean.class).size());

    }

}
