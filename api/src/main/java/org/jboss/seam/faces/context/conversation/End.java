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
 * Ends a persistent {@link Conversation}.
 * 
 * <p>
 * <b>Note:</b> Unless the exception is of a permitted type, if this method throws an exception, the conversation will not be
 * ended.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ConversationBoundary
@InterceptorBinding
@Target({ METHOD, TYPE })
@Retention(RUNTIME)
public @interface End {
    /**
     * Sets the exception types for which, when encountered during a method invocation, the {@link Conversation} will still end.
     * (In other words: These exceptions do not abort @{@link End})
     * <p>
     * <b>By default:</b> { empty array } - all encountered exceptions will cause the {@link Conversation} to remain open.
     */
    @Nonbinding
    Class<? extends Exception>[] permit() default {};

}