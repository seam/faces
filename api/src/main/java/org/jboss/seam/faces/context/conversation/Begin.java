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
 * Begins a persistent {@link Conversation}.
 * <p/>
 * <p/>
 * <b>Note:</b> Unless the exception is of a permitted type, if this method throws an exception, the conversation will not
 * begin.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ConversationBoundary
@InterceptorBinding
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Begin {
    /**
     * Sets the new {@link Conversation} ID. Seam will Generate a conversation ID if left blank.
     * <p/>
     * If a conversation with the ID already exists... TODO what should we do?
     * <p/>
     * TODO test default conversation ID functionality
     */
    @Nonbinding String id() default "";

    /**
     * Sets the {@link Conversation} timeout period, in milliseconds (E.g.: 5000 = 5 seconds.)
     * <p/>
     */
    @Nonbinding long timeout() default -1;

    /**
     * Sets the exception types for which, when encountered during a method invocation, the {@link Conversation} will still
     * begin. (In other words: Permitted exceptions do not abort @{@link Begin})
     * <p/>
     * <b>By default:</b> { empty array } - all encountered exceptions will prevent the {@link Conversation} from beginning.
     */
    @Nonbinding Class<? extends Exception>[] permit() default {};

}
