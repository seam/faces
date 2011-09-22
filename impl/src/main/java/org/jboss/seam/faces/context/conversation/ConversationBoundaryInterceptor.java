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
package org.jboss.seam.faces.context.conversation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.seam.logging.Logger;
import org.jboss.seam.solder.reflection.AnnotationInspector;

/**
 * Intercepts methods annotated as Conversational entry points: @{@link Begin} and @{@link End}
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 * @author <a href="mailto:ssachtleben@gmail.com">Sebastian Sachtleben</a>
 */
@ConversationBoundary
@Interceptor
public class ConversationBoundaryInterceptor implements Serializable {
    private static final long serialVersionUID = -2729227895205287477L;

    Logger log = Logger.getLogger(ConversationBoundaryInterceptor.class);

    @Inject
    Conversation conversation;
    @Inject
    private BeanManager beanManager;

    @AroundInvoke
    public Object around(final InvocationContext ctx) throws Exception {
        Object result = null;

        try {
            if (AnnotationInspector.isAnnotationPresent(ctx.getMethod(), Begin.class, beanManager)) {
                beginConversation(ctx);
            }

            result = ctx.proceed();

            if (AnnotationInspector.isAnnotationPresent(ctx.getMethod(), End.class, beanManager)) {
                endConversation(ctx);
            }
        } catch (Exception e) {
            handleExceptionBegin(ctx, e);
            handleExceptionEnd(ctx, e);
            throw e;
        }

        return result;
    }

    private void handleExceptionBegin(final InvocationContext ctx, final Exception e) {
        if (AnnotationInspector.isAnnotationPresent(ctx.getMethod(), Begin.class, beanManager)) {
            List<? extends Class<? extends Exception>> typesPermittedByBegin = getPermittedExceptionTypesBegin(ctx.getMethod());
            for (Class<? extends Exception> type : typesPermittedByBegin) {
                if (type.isInstance(e) == false) {
                    log.debugf(
                            "Aborting conversation: (#0) for method: (#1.#2(...)) - Encountered Exception of type (#4), which is not in the list of exceptions permitted by @Begin.",
                            new Object[]{conversation.getId(), ctx.getMethod().getDeclaringClass().getName(),
                                    ctx.getMethod().getName(), e.getClass().getName()});
                    conversation.end();
                }
            }
        }

    }

    private void handleExceptionEnd(final InvocationContext ctx, final Exception e) {
        if (AnnotationInspector.isAnnotationPresent(ctx.getMethod(), End.class, beanManager)) {
            List<? extends Class<? extends Exception>> typesPermittedByEnd = getPermittedExceptionTypesEnd(ctx.getMethod());
            boolean permitted = false;
            for (Class<? extends Exception> type : typesPermittedByEnd) {
                if (type.isInstance(e)) {
                    permitted = true;
                    conversation.end();
                }
            }
            if (!permitted) {
                log.debugf(
                        "Conversation will remain open: (#0) for method: (#1.#2(...)) - Encountered Exception of type (#4), which is not in the list of exceptions permitted by @End.",
                        new Object[]{conversation.getId(), ctx.getMethod().getDeclaringClass().getName(),
                                ctx.getMethod().getName(), e.getClass().getName()});
            }
        }
    }

    private void beginConversation(final InvocationContext ctx) throws Exception {
        if (!conversation.isTransient()) {
            log.debugf("Conversation (#0) is already long running before method: (#1.#2(...))", new Object[]{
                    conversation.getId(), ctx.getMethod().getDeclaringClass().getName(), ctx.getMethod().getName()});
            return;
        }
        Begin beginAnnotation = AnnotationInspector.getAnnotation(ctx.getMethod(), Begin.class, beanManager);
        String cid = beginAnnotation.id();
        if ((cid != null) && !"".equals(cid)) {
            conversation.begin(cid);
        } else {
            conversation.begin();
        }

        long timeout = beginAnnotation.timeout();
        if (timeout != -1) {
            conversation.setTimeout(timeout);
        }

        log.debugf("Began conversation: (#0) before method: (#1.#2(...))", new Object[]{conversation.getId(),
                ctx.getMethod().getDeclaringClass().getName(), ctx.getMethod().getName()});
    }

    private void endConversation(final InvocationContext ctx) {
    	if (conversation.isTransient()) {
            log.debugf("No conversation found after method: (#0.#1(...))", new Object[]{
                    ctx.getMethod().getDeclaringClass().getName(), ctx.getMethod().getName()});
    		return;
    	}
        log.debugf("Ending conversation: (#0) after method: (#1.#2(...))", new Object[]{conversation.getId(),
                ctx.getMethod().getDeclaringClass().getName(), ctx.getMethod().getName()});
        conversation.end();
    }

    private List<? extends Class<? extends Exception>> getPermittedExceptionTypesBegin(final Method m) {
        return Arrays.asList(AnnotationInspector.getAnnotation(m, Begin.class, beanManager).permit());
    }

    private List<? extends Class<? extends Exception>> getPermittedExceptionTypesEnd(final Method m) {
        return Arrays.asList(AnnotationInspector.getAnnotation(m, End.class, beanManager).permit());
    }
}
