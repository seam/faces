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
package org.jboss.seam.faces.test;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;

/**
 * Provide a mocked conversation object for use in Unit tests. This entire class
 * is a no-op; it does <i>nothing</i>.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RequestScoped
public class MockConversation implements Conversation
{
   private long timeout;
   private String id;
   private boolean persistent;

   public void begin()
   {
      this.id = "generated";
      persistent = true;
   }

   public void begin(final String id)
   {
      this.id = id;
      persistent = true;
   }

   public void end()
   {
      persistent = false;
   }

   public String getId()
   {
      return id;
   }

   public long getTimeout()
   {
      return timeout;
   }

   public boolean isTransient()
   {
      return this.persistent == false;
   }

   public void setTimeout(final long milliseconds)
   {
      this.timeout = milliseconds;
   }

}
