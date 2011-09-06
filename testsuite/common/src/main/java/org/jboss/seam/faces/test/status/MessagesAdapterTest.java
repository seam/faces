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
package org.jboss.seam.faces.test.status;

import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.event.PhaseEventBridge;
import org.jboss.seam.faces.status.MessagesAdapter;
import org.jboss.seam.faces.test.MockLogger;
import org.jboss.seam.faces.test.MockLoggerProducer;
import org.jboss.seam.faces.test.PhaseTestBase;
import org.jboss.seam.faces.test.context.MockFlashContext;
import org.jboss.seam.international.locale.DefaultLocaleProducer;
import org.jboss.seam.international.locale.UserLocaleProducer;
import org.jboss.seam.international.status.ApplicationBundles;
import org.jboss.seam.international.status.Bundles;
import org.jboss.seam.international.status.MessageFactory;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.MessagesImpl;
import org.jboss.seam.international.status.builder.BundleTemplateMessageImpl;
import org.jboss.seam.international.status.builder.TemplateMessageImpl;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.faces.stub.faces.StubFacesContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class MessagesAdapterTest extends PhaseTestBase {
    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(MessagesAdapter.class, MessagesImpl.class, MockFlashContext.class, MessageFactory.class,
                        TemplateMessageImpl.class, BundleTemplateMessageImpl.class, Bundles.class, PhaseEventBridge.class,
                        MockLogger.class, MockLoggerProducer.class, ApplicationBundles.class, UserLocaleProducer.class, DefaultLocaleProducer.class)
                .addAsManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
    }

    @Inject
    Messages messages;

    String text = "Hey! This is a message";

    @Before
    public void before() {
        facesContext = new StubFacesContext();
    }

    @Test
    public void testMessagesAreTransferredBeforeRenderResponse() {
        messages.add(messages.info(text));
        assertEquals(1, messages.getAll().size());

        fireBeforePhase(PhaseId.RENDER_RESPONSE);

        assertTrue(messages.getAll().isEmpty());
        assertNotNull(facesContext.getMessages());
        assertEquals(text, facesContext.getMessages().next().getSummary());

    }

    @Test
    public void testMessageTargetsTransferredToFacesMessageComponentId() {
        messages.add(messages.info(text).targets("component"));
        assertEquals(1, messages.getAll().size());

        fireBeforePhase(PhaseId.RENDER_RESPONSE);

        assertTrue(messages.getAll().isEmpty());
        assertNotNull(facesContext.getMessages("component"));
        assertEquals(text, facesContext.getMessages("component").next().getSummary());
    }
}
