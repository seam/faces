package org.jboss.seam.faces.context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.NormalScope;
import javax.faces.event.PhaseId;

/**
 * Defines a CDI bean as Render-scoped. Data put in the render scope will survive until the next JSF
 * {@link PhaseId#RENDER_RESPONSE} is completed, at which point the {@link RenderContext} will be destroyed along with all
 * references to its contents.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@NormalScope
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RenderScoped {

}
