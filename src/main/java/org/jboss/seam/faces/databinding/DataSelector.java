package org.jboss.seam.faces.databinding;

import java.lang.annotation.Annotation;

/**
 * Allows extraction of the selected item
 * from some "wrapper type".
 * 
 * @author Gavin King
 *
 * @param <In> the annotation type
 * @param <WrapperType> the wrapper type
 */
public interface DataSelector<In extends Annotation, WrapperType>
{
   String getVariableName(In in);
   Object getSelection(In in, WrapperType wrapper);
}
