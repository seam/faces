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
package org.jboss.seam.faces.context.conversation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.MockConversation;
import org.jboss.seam.faces.MockLogger;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RunWith(Arquillian.class)
public class ConversationBoundaryInterceptorTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(ConversationBoundaryInterceptor.class, ConversationalBean.class, MockLogger.class, MockConversation.class).addManifestResource(ConversationBoundaryInterceptorTest.class.getPackage().getName().replaceAll("\\.", "/") + "/ConversationBoundaryInterceptorTest-beans.xml", ArchivePaths.create("beans.xml"));
   }

   @Inject
   Conversation conversation;

   @Inject
   private ConversationalBean interceptedBean;

   @Test
   public void testConversationStarted()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningInsideMethodCall());

      interceptedBean.beginConversation();

      assertFalse(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningInsideMethodCall());
   }

   @Test
   public void testConversationStartedWithTimeout()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningInsideMethodCall());

      interceptedBean.beginConversation();

      assertEquals(1000, conversation.getTimeout());
      assertFalse(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningInsideMethodCall());
   }

   @Test
   public void testConversationBeginsAndEnds()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningDuringInvocation2());

      interceptedBean.beginAndEndConversation();

      assertTrue(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningDuringInvocation2());
   }

   @Test
   public void testConversationAbortsBeginOnFatalException()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningDuringInvocation3());

      try
      {
         interceptedBean.beginAndThrowFatalException();
      }
      catch (Exception e)
      {
         // expected
      }

      assertTrue(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningDuringInvocation3());
   }

   @Test
   public void testConversationBeginsOnPermittedException()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningDuringInvocation4());

      try
      {
         interceptedBean.beginAndThrowPermittedException();
      }
      catch (Exception e)
      {
         // expected
      }

      assertFalse(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningDuringInvocation4());
   }

   @Test
   public void testConversationAbortsEndOnFatalException()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningDuringInvocation5());

      try
      {
         interceptedBean.begin();
         interceptedBean.endAndThrowFatalException();
      }
      catch (Exception e)
      {
         // expected
      }

      assertFalse(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningDuringInvocation5());
   }

   @Test
   public void testConversationEndsOnPermittedException()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningDuringInvocation6());

      try
      {
         interceptedBean.begin();
         interceptedBean.endAndThrowPermittedException();
      }
      catch (Exception e)
      {
         // expected
      }

      assertTrue(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningDuringInvocation6());
   }
}
