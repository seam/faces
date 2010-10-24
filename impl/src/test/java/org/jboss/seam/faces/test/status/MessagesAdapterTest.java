/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.faces.test.status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.event.PhaseEventBridge;
import org.jboss.seam.faces.status.MessagesAdapter;
import org.jboss.seam.faces.test.MockLogger;
import org.jboss.seam.faces.test.PhaseTestBase;
import org.jboss.seam.faces.test.context.MockFlashContext;
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

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RunWith(Arquillian.class)
public class MessagesAdapterTest extends PhaseTestBase
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return ShrinkWrap
               .create(JavaArchive.class)
               .addClasses(MessagesAdapter.class, MessagesImpl.class, MockFlashContext.class, MessageFactory.class,
                        TemplateMessageImpl.class, BundleTemplateMessageImpl.class, Bundles.class,
                        PhaseEventBridge.class, MockLogger.class)
               .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject
   Messages messages;

   String text = "Hey! This is a message";

   @Before
   public void before()
   {
      facesContext = new StubFacesContext();
   }

   @Test
   public void testMessagesAreTransferredBeforeRenderResponse()
   {
      messages.add(messages.info(text));
      assertEquals(1, messages.getAll().size());

      fireBeforePhase(PhaseId.RENDER_RESPONSE);

      assertTrue(messages.getAll().isEmpty());
      assertNotNull(facesContext.getMessages());
      assertEquals(text, facesContext.getMessages().next().getSummary());

   }

   @Test
   public void testMessageTargetsTransferredToFacesMessageComponentId()
   {
      messages.add(messages.info(text).targets("component"));
      assertEquals(1, messages.getAll().size());

      fireBeforePhase(PhaseId.RENDER_RESPONSE);

      assertTrue(messages.getAll().isEmpty());
      assertNotNull(facesContext.getMessages("component"));
      assertEquals(text, facesContext.getMessages("component").next().getSummary());
   }
}
