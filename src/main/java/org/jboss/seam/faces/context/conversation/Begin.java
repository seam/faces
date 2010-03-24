package org.jboss.seam.faces.context.conversation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.Conversation;
import javax.interceptor.InterceptorBinding;

/**
 * Marks the beginning of a persistent {@link Conversation}.
 * 
 *<p>
 * <b>Note:</b> If this method throws an exception, the conversation will be
 * discarded, unless the exception is annotated with @
 * {@link RetainsConversation}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@InterceptorBinding
@Target( { METHOD, TYPE })
@Retention(RUNTIME)
public @interface Begin
{
   /**
    * The new conversation ID. Seam will Generate a conversation ID if left
    * blank. If a conversation with the ID already exists, TODO what should we
    * do?
    */
   String id() default "";
}