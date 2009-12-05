//$Id: DataModelSelection.java 2025 2006-09-18 16:43:02Z gavin $
package org.jboss.seam.faces.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jboss.seam.faces.databinding.DataModelSelector;

/**
 * Injects the selected row data of a DataModel. 
 * Intended for use with @DataModel.
 * 
 * @author Gavin King
 * @see DataModel
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
@DataSelectorClass(DataModelSelector.class)
public @interface DataModelSelection
{
   /**
    * The context variable name of the DataModel. Defaults 
    * to the name for the outjected @DataModel if there
    * is exactly one @DataModel for the component.
    */
   String value() default "";
}
