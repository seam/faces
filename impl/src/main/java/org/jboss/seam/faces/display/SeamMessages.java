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

package org.jboss.seam.faces.display;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.event.PhaseEvent;

import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.RenderResponse;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
@SessionScoped
public class SeamMessages implements Messages, Serializable
{
   private static final long serialVersionUID = -2908193057765795662L;
   private final Set<Message> messages = Collections.synchronizedSet(new HashSet<Message>());

   @SuppressWarnings("unused")
   private void convert(@Observes @Before @RenderResponse final PhaseEvent event)
   {
      for (Message m : messages)
      {
         event.getFacesContext().addMessage(m.getClientId(), new FacesMessage(m.getLevel().getSeverity(), m.getMessage(), m.getDetails()));
      }
      clear();
   }

   public void clear()
   {
      messages.clear();
   }

   public Message add(final Level level)
   {
      Message result = new SeamMessage(level);
      messages.add(result);
      return result;
   }

   public Set<Message> getAll()
   {
      Set<Message> result;
      synchronized (messages)
      {
         result = Collections.unmodifiableSet(messages);
      }
      return result;
   }

}
