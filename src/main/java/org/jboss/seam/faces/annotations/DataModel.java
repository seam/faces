//$Id: DataModel.java 2169 2006-10-10 03:48:19Z gavin $
package org.jboss.seam.faces.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jboss.seam.faces.databinding.DataModelBinder;

/**
 * Outjects a collection to the same scope as the owning component
 * (or to the EVENT scope in the case of a stateless component),
 * after wrapping as a JSF DataModel (a List as a ListDataModel, a
 * Map as a MapDataModel, a Set as a SetDataModel, an array as an
 * ArrayDataModel). Note that the List, Map, Set or array
 * will be re-wrapped and re-outjected each time the current
 * component value is different to the value held by the
 * context variable as determined by calling equals() on the
 * underlying collection.
 * 
 * @author Gavin King
 * 
 * @see org.jboss.seam.jsf.ListDataModel
 * @see org.jboss.seam.jsf.MapDataModel
 * @see org.jboss.seam.jsf.SetDataModel
 * @see org.jboss.seam.jsf.ArrayDataModel
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
@DataBinderClass(DataModelBinder.class)
public @interface DataModel
{
   /**
    * The context variable name. Defaults to the name of
    * the annotated field or getter method.
    */
   String value() default "";
   
   /**
    * Specifies the scope to outject the DataModel to.
    * If no scope is explicitly specified, the scope of
    * the component with the @DataModel attribute is used.
    * But if the component scope is STATELESS, the EVENT
    * scope is used.
    * 
    */
   Class<? extends Annotation> scope();
}
