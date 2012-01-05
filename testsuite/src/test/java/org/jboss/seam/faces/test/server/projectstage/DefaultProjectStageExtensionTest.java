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
package org.jboss.seam.faces.test.server.projectstage;

import static junit.framework.Assert.assertEquals;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.projectstage.ProjectStageExtension;
import org.jboss.seam.faces.test.server.projectstage.beans.DevelopmentStageBean;
import org.jboss.seam.faces.test.server.projectstage.beans.NoStageRestrictionBean;
import org.jboss.seam.faces.test.server.projectstage.beans.ProductionStageBean;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * This tests checks if the {@link ProjectStageExtension} works correctly if no project stage can be detected. In this case the
 * extension assumes the Production stage.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
@RunWith(Arquillian.class)
public class DefaultProjectStageExtensionTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return ProjectStageBaseArchive.getBaseArchive()
                .addClasses(DevelopmentStageBean.class, ProductionStageBean.class, NoStageRestrictionBean.class);
    }

    @Inject
    private BeanManager beanManager;

    @Test
    public void testActivatedBeansInDevelopmentStageFromWebXml() {

        // The @Development annotated bean is vetoed, all others will be active
        assertEquals(0, beanManager.getBeans(DevelopmentStageBean.class).size());
        assertEquals(1, beanManager.getBeans(ProductionStageBean.class).size());
        assertEquals(1, beanManager.getBeans(NoStageRestrictionBean.class).size());

    }

}
