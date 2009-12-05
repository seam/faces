package org.jboss.seam.faces.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jboss.seam.faces.databinding.DataSelector;

/**
 * Meta-annotation that specifies that an annotation
 * is a dataselection annotation, ie. that it results
 * in injection of the selected item of some databound
 * data.
 * 
 * @see org.jboss.seam.databinding.DataSelector
 * @author Gavin King
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
@Documented
public @interface DataSelectorClass
{
   Class<? extends DataSelector> value();
}
