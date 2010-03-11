package javax.faces.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import javax.enterprise.context.NormalScope;

/**
 * Defines a CDI bean as Flash scoped. A bean put in the JSF2 flash will survive
 * one page transition, or navigation, then be cleared.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@NormalScope
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FlashScoped {

}
