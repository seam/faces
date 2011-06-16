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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.Conversation;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Ends a persistent {@link Conversation}.
 * <p/>
 * <p/>
 * <b>Note:</b> Unless the exception is of a permitted type, if this method throws an exception, the conversation will not be
 * ended.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ConversationBoundary
@InterceptorBinding
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface End {
    /**
     * Sets the exception types for which, when encountered during a method invocation, the {@link Conversation} will still end.
     * (In other words: These exceptions do not abort @{@link End})
     * <p/>
     * <b>By default:</b> { empty array } - all encountered exceptions will cause the {@link Conversation} to remain open.
     */
    @Nonbinding Class<? extends Exception>[] permit() default {};

}
