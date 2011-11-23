package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.view.action.el.ElViewAction;
import org.jboss.seam.faces.view.action.el.ElViewActionHandlerProvider;

/**
 * Used as a meta-annotation to use on a view action annotation. This view action annotation will be applied to a given view
 * (see <code>ViewConfig</code>) with a given view action annotation.
 * 
 * <p>
 * When creating a view action annotation, you must also implement ViewActionHandlerProvider and ViewActionHandler interfaces.
 * Those implementations will execute the action itself.
 * </p>
 * 
 * <p>
 * viewAction annotations can be further customized by
 * <ul>
 * <li>using the following attributes order (type Long), phase (type Class) and before (class Boolean) attributes.</li>
 * <li>using the meta-annotations {@link Order}, {@link Before}, {@link After} or any annotations from package
 * <code>org.jboss.seam.faces.event.qualifier</code>.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Sample usage:
 * 
 * </p>
 * 
 * <p>
 * Sample view action annotation :
 * 
 * <pre>
 * &#064;ViewAction(BeginConversationHandlerProvider.class)
 * &#064;Before
 * &#064;ApplyRequestValues
 * &#064;Order(OrderDefault.DEFAULT - 10)
 * &#064;Target(ElementType.FIELD)
 * &#064;Retention(RetentionPolicy.RUNTIME)
 * &#064;Documented
 * public @interface BeginConversation {
 *     boolean join() default false;
 * }
 * </pre>
 * 
 * Sample view action handler provider :
 * 
 * <pre>
 * public class BeginConversationHandlerProvider extends SimpleViewActionHandlerProvider&lt;BeginConversation&gt; {
 *     private boolean join;
 * 
 *     &#064;Inject
 *     private Conversation conversation;
 * 
 *     &#064;Override
 *     public void doInitialize(BeginConversation annotation) {
 *         join = annotation.join();
 *     }
 * 
 *     &#064;Override
 *     public Object execute() {
 *         if (join &amp;&amp; !conversation.isTransient()) {
 *             return null;
 *         }
 *         conversation.begin();
 *         return null;
 *     }
 * }
 * </pre>
 * 
 * Sample usage :
 * 
 * <pre>
 * &#064;ViewConfig
 * public interface ViewConfigEnum {
 *     static enum Pages {
 *         &#064;ViewPattern(&quot;/happy/done.xhtml&quot;)
 *         &#064;BeginConversation
 *         HAPPY_DONE(),
 *     }
 * }
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * See {@link ElViewAction} (the view action annotation) and {@link ElViewActionHandlerProvider} for sample usage.
 * </p>
 * 
 * @author Adriàn Gonzalez
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewAction {
    /** class implementing view action custom logic. */
    Class<? extends ViewActionHandlerProvider<? extends Annotation>> value();
}
