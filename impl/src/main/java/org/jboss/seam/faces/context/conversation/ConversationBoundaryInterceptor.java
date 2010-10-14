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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.util.Annotations;

/**
 * Intercepts methods annotated as Conversational entry points: @{@link Begin}
 * and @{@link End}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ConversationBoundary
@Interceptor
public class ConversationBoundaryInterceptor implements Serializable
{
   private static final long serialVersionUID = -2729227895205287477L;

   Logger log = Logger.getLogger(ConversationBoundaryInterceptor.class);

   @Inject
   Conversation conversation;

   @AroundInvoke
   public Object around(final InvocationContext ctx) throws Exception
   {
      Object result = null;

      try
      {
         if (Annotations.isAnnotationPresent(ctx.getMethod(), Begin.class))
         {
            beginConversation(ctx);
         }

         result = ctx.proceed();

         if (Annotations.isAnnotationPresent(ctx.getMethod(), End.class))
         {
            endConversation(ctx);
         }
      }
      catch (Exception e)
      {
         handleExceptionBegin(ctx, e);
         handleExceptionEnd(ctx, e);
         throw e;
      }

      return result;
   }

   private void handleExceptionBegin(final InvocationContext ctx, final Exception e)
   {
      if (Annotations.isAnnotationPresent(ctx.getMethod(), Begin.class))
      {
         List<? extends Class<? extends Exception>> typesPermittedByBegin = getPermittedExceptionTypesBegin(ctx.getMethod());
         for (Class<? extends Exception> type : typesPermittedByBegin)
         {
            if (type.isInstance(e) == false)
            {
               log.debug("Aborting conversation: (#0) for method: (#1.#2(...)) - Encountered Exception of type (#4), which is not in the list of exceptions permitted by @Begin.", new Object[] { conversation.getId(), ctx.getMethod().getDeclaringClass().getName(), ctx.getMethod().getName(), e.getClass().getName() });
               conversation.end();
            }
         }
      }

   }

   private void handleExceptionEnd(final InvocationContext ctx, final Exception e)
   {
      if (Annotations.isAnnotationPresent(ctx.getMethod(), End.class))
      {
         List<? extends Class<? extends Exception>> typesPermittedByEnd = getPermittedExceptionTypesEnd(ctx.getMethod());
         boolean permitted = false;
         for (Class<? extends Exception> type : typesPermittedByEnd)
         {
            if (type.isInstance(e))
            {
               permitted = true;
               conversation.end();
            }
         }
         if (!permitted)
         {
            log.debug("Conversation will remain open: (#0) for method: (#1.#2(...)) - Encountered Exception of type (#4), which is not in the list of exceptions permitted by @End.", new Object[] { conversation.getId(), ctx.getMethod().getDeclaringClass().getName(), ctx.getMethod().getName(), e.getClass().getName() });
         }
      }
   }

   private void beginConversation(final InvocationContext ctx) throws Exception
   {
      String cid = Annotations.getAnnotation(ctx.getMethod(), Begin.class).id();
      if ((cid != null) && !"".equals(cid))
      {
         conversation.begin(cid);
      }
      else
      {
         conversation.begin();
      }

      long timeout = Annotations.getAnnotation(ctx.getMethod(), Begin.class).timeout();
      if (timeout != -1)
      {
         conversation.setTimeout(timeout);
      }

      log.debug("Began conversation: (#0) before method: (#1.#2(...))", new Object[] { conversation.getId(), ctx.getMethod().getDeclaringClass().getName(), ctx.getMethod().getName() });
   }

   private void endConversation(final InvocationContext ctx)
   {
      log.debug("Ending conversation: (#0) after method: (#1.#2(...))", new Object[] { conversation.getId(), ctx.getMethod().getDeclaringClass().getName(), ctx.getMethod().getName() });
      conversation.end();
   }

   private List<? extends Class<? extends Exception>> getPermittedExceptionTypesBegin(final Method m)
   {
      return Arrays.asList(Annotations.getAnnotation(m, Begin.class).permit());
   }

   private List<? extends Class<? extends Exception>> getPermittedExceptionTypesEnd(final Method m)
   {
      return Arrays.asList(Annotations.getAnnotation(m, End.class).permit());
   }
}
