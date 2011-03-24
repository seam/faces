package org.jboss.seam.faces.context.conversation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * Parent annotation for @{@link Begin} and @{@link End}
 * <p>
 * <b>Note:</b> This should never be used.
 * <p>
 * TODO: Should we warn at startup if @{@link Begin} and @{@link End} are used together on the same method?
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@InterceptorBinding
@Inherited
@Target({ METHOD, TYPE })
@Retention(RUNTIME)
@interface ConversationBoundary {
}