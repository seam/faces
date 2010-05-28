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

package org.jboss.seam.faces.context;

/**
 * A context that lives from Restore View to the next Render Response.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public interface FlashContext
{

   /**
    * Returns true if the current {@link FlashContext} contains no data.
    */
   boolean isEmpty();

   /**
    * Return the current ID of this request's {@link FlashContext}. If the ID
    * has not yet been set as part of a redirect, the ID will be null.
    */
   String getId();

   /**
    * Get a key value pair from the {@link FlashContext}.
    */
   Object get(String key);

   /**
    * Put a key value pair into the {@link FlashContext}.
    */
   void put(String key, Object value);

}
