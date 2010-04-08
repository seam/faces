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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.Conversation;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * Begins a persistent {@link Conversation}.
 * 
 *<p>
 * <b>Note:</b> Unless the exception is of a permitted type, if this method
 * throws an exception, the conversation will not begin.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ConversationBoundary
@InterceptorBinding
@Target( { METHOD, TYPE })
@Retention(RUNTIME)
public @interface Begin
{
   /**
    * Sets the new {@link Conversation} ID. Seam will Generate a conversation ID
    * if left blank.
    * <p>
    * If a conversation with the ID already exists... TODO what should we do?
    * <p>
    * TODO test default conversation ID functionality
    */
   @Nonbinding
   String id() default "";

   /**
    * Sets the {@link Conversation} timeout period, in milliseconds (E.g.: 5000
    * = 5 seconds.)
    * <p>
    */
   @Nonbinding
   long timeout() default -1;

   /**
    * Sets the exception types for which, when encountered during a method
    * invocation, the {@link Conversation} will still begin. (In other words:
    * Permitted exceptions do not abort @{@link Begin})
    * <p>
    * <b>By default:</b> { empty array } - all encountered exceptions will
    * prevent the {@link Conversation} from beginning.
    */
   @Nonbinding
   Class<? extends Exception>[] permit() default {};

}