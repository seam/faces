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

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class SeamMessage implements Message
{
   private static final long serialVersionUID = 6650116552438358826L;

   private String message;
   private String details;
   private String clientId;
   private final Level level;

   public SeamMessage(final Level level)
   {
      this.level = level;
   }

   @Override
   public String toString()
   {
      return "SeamMessage [clientId=" + clientId + ", details=" + details + ", level=" + level + ", message=" + message + "]";
   }

   public Message component(final String clientId)
   {
      this.clientId = clientId;
      return this;
   }

   public Message details(final String details)
   {
      this.details = details;
      return this;
   }

   public Message summary(final String message)
   {
      this.message = message;
      return this;
   }

   /*
    * Getters & Setters
    */
   public Level getLevel()
   {
      return level;
   }

   public String getMessage()
   {
      return message;
   }

   public String getDetails()
   {
      return details;
   }

   public String getClientId()
   {
      return clientId;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
      result = prime * result + ((details == null) ? 0 : details.hashCode());
      result = prime * result + ((level == null) ? 0 : level.hashCode());
      result = prime * result + ((message == null) ? 0 : message.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      SeamMessage other = (SeamMessage) obj;
      if (clientId == null)
      {
         if (other.clientId != null)
         {
            return false;
         }
      }
      else if (!clientId.equals(other.clientId))
      {
         return false;
      }
      if (details == null)
      {
         if (other.details != null)
         {
            return false;
         }
      }
      else if (!details.equals(other.details))
      {
         return false;
      }
      if (level == null)
      {
         if (other.level != null)
         {
            return false;
         }
      }
      else if (!level.equals(other.level))
      {
         return false;
      }
      if (message == null)
      {
         if (other.message != null)
         {
            return false;
         }
      }
      else if (!message.equals(other.message))
      {
         return false;
      }
      return true;
   }

}
