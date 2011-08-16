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
package org.jboss.seam.faces.test.security;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.security.RestrictAtPhaseDefault;
import org.jboss.seam.faces.security.SecurityPhaseListener;
import org.jboss.seam.faces.test.view.config.annotation.RestrictedAtRestoreViewLiteral;
import org.jboss.seam.faces.test.view.config.annotation.RestrictedDefaultLiteral;
import org.jboss.seam.faces.test.view.config.annotation.ViewConfigEnum;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.seam.security.events.AuthorizationCheckEvent;
import org.jboss.seam.security.extension.SecurityExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@RunWith(Arquillian.class)
public class SecurityPhaseListenerTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addClass(ViewConfigStoreImpl.class)
                .addClass(SecurityPhaseListener.class)
                .addClass(AuthorizationCheckEvent.class)
                .addClass(SecurityBindingType.class)
                .addClass(SecurityExtension.class)
                .addClass(PhaseIdType.class)
                .addClass(RestrictAtPhaseDefault.class)
                .addClass(IdentityMock.class)
                .addPackage(RenderResponse.class.getPackage())
                .addPackage(ViewConfigEnum.class.getPackage())
                .addAsManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
        return archive;
    }

    @Inject
    private ViewConfigStore store;

    @Inject
    private SecurityPhaseListener listener;

    @Test
    public void testIsAnnotationApplicableToPhase() {
        Assert.assertEquals(true, listener.isAnnotationApplicableToPhase(new RestrictedAtRestoreViewLiteral(), PhaseIdType.RESTORE_VIEW, RestrictAtPhaseDefault.DEFAULT_PHASES));
        Assert.assertEquals(false, listener.isAnnotationApplicableToPhase(new RestrictedAtRestoreViewLiteral(), PhaseIdType.INVOKE_APPLICATION, RestrictAtPhaseDefault.DEFAULT_PHASES));
        Assert.assertEquals(false, listener.isAnnotationApplicableToPhase(new RestrictedAtRestoreViewLiteral(), PhaseIdType.RENDER_RESPONSE, RestrictAtPhaseDefault.DEFAULT_PHASES));
        Assert.assertEquals(false, listener.isAnnotationApplicableToPhase(new RestrictedDefaultLiteral(), PhaseIdType.RESTORE_VIEW, RestrictAtPhaseDefault.DEFAULT_PHASES));
        Assert.assertEquals(true, listener.isAnnotationApplicableToPhase(new RestrictedDefaultLiteral(), PhaseIdType.INVOKE_APPLICATION, RestrictAtPhaseDefault.DEFAULT_PHASES));
        Assert.assertEquals(true, listener.isAnnotationApplicableToPhase(new RestrictedDefaultLiteral(), PhaseIdType.RENDER_RESPONSE, RestrictAtPhaseDefault.DEFAULT_PHASES));
    }


    @Test
    public void testIsRestrictPhase() {
        List<? extends Annotation> restrict;
        restrict = listener.getRestrictionsForPhase(PhaseIdType.RESTORE_VIEW, "/qualified/yes.xhtml");
        Assert.assertEquals(1, restrict.size());

        restrict = listener.getRestrictionsForPhase(PhaseIdType.INVOKE_APPLICATION, "/qualified/yes.xhtml");
        Assert.assertEquals(1, restrict.size());

        restrict = listener.getRestrictionsForPhase(PhaseIdType.RESTORE_VIEW, "/qualified/yes.xhtml");
        Assert.assertEquals(1, restrict.size());

        restrict = listener.getRestrictionsForPhase(PhaseIdType.RENDER_RESPONSE, "/qualified/no.xhtml");
        Assert.assertEquals(null, restrict);

        restrict = listener.getRestrictionsForPhase(PhaseIdType.INVOKE_APPLICATION, "/qualified/no.xhtml");
        Assert.assertEquals(1, restrict.size());

        restrict = listener.getRestrictionsForPhase(PhaseIdType.RENDER_RESPONSE, "/happy/cat.xhtml");
        Assert.assertEquals(null, restrict);

    }

    @Test
    public void testGetDefaultPhases() {
        PhaseIdType[] defaults;
        defaults = listener.getDefaultPhases("/qualified/no.xhtml");
        Assert.assertEquals(1, defaults.length);
        Assert.assertEquals(PhaseIdType.INVOKE_APPLICATION, defaults[0]);
    }
}
