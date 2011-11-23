package org.jboss.seam.faces.view.action.el;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.view.action.Order;
import org.jboss.seam.faces.view.action.OrderDefault;
import org.jboss.seam.faces.view.action.ViewAction;

@ViewAction(BeginConversationHandlerProvider.class)
@Before
@ApplyRequestValues
@Order(OrderDefault.DEFAULT-10)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeginConversation {
    boolean join() default false;
}
