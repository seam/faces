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

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.seam.faces.SeamFacesException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public @RequestScoped class ConversationalBean
{
   @Inject
   Conversation conversation;

   private boolean conversationLongRunningDuringInvocation;
   private boolean conversationLongRunningDuringInvocation2;
   private boolean conversationLongRunningDuringInvocation3;
   private boolean conversationLongRunningDuringInvocation4;
   private boolean conversationLongRunningDuringInvocation5;

   private boolean conversationLongRunningDuringInvocation6;

   @Begin
   public void begin()
   {
   }

   @Begin(timeout = 1000)
   public void beginConversation()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation = true;
      }
   }

   @Begin
   @End
   public void beginAndEndConversation()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation2 = true;
      }
   }

   @Begin(permit = { SeamFacesException.class })
   public void beginAndThrowFatalException()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation3 = true;
      }
      throw new RuntimeException("A vanilla exception.");
   }

   @Begin(permit = { SeamFacesException.class })
   public void beginAndThrowPermittedException()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation4 = true;
      }
      throw new SeamFacesException("Just so it's not a vanilla Exception.");
   }

   @End(permit = { SeamFacesException.class })
   public void endAndThrowFatalException()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation5 = true;
      }
      throw new RuntimeException("A vanilla exception.");
   }

   @End(permit = { SeamFacesException.class })
   public void endAndThrowPermittedException()
   {
      if (!conversation.isTransient())
      {
         conversationLongRunningDuringInvocation6 = true;
      }
      throw new SeamFacesException("A vanilla exception.");
   }

   public boolean isConversationLongRunningInsideMethodCall()
   {
      return conversationLongRunningDuringInvocation;
   }

   public boolean isConversationLongRunningDuringInvocation2()
   {
      return conversationLongRunningDuringInvocation2;
   }

   public boolean isConversationLongRunningDuringInvocation3()
   {
      return conversationLongRunningDuringInvocation3;
   }

   public boolean isConversationLongRunningDuringInvocation4()
   {
      return conversationLongRunningDuringInvocation4;
   }

   public boolean isConversationLongRunningDuringInvocation5()
   {
      return conversationLongRunningDuringInvocation5;
   }

   public boolean isConversationLongRunningDuringInvocation6()
   {
      return conversationLongRunningDuringInvocation6;
   }

}
