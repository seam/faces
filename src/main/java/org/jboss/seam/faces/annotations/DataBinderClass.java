package org.jboss.seam.faces.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jboss.seam.faces.databinding.DataBinder;

/**
 * Meta-annotation that specifies that an annotation
 * is a databinding annotation, ie. that it results
 * in outjection of a wrapped representation of the
 * annotated component attribute value.
 * 
 * @see org.jboss.seam.databinding.DataBinder
 * @author Gavin King
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
@Documented
public @interface DataBinderClass
{
   Class<? extends DataBinder> value();
}
