package org.jboss.seam.faces.databinding;

import java.lang.annotation.Annotation;

/**
 * Allows some "bound type" to be exposed to 
 * the user interface via a "wrapper type".
 * 
 * @author Gavin King
 *
 * @param <Out> the annotation type
 * @param <Type> the bound type
 * @param <WrapperType> the wrapper type
 */
public interface DataBinder<Out extends Annotation, Type, WrapperType>
{
   String getVariableName(Out out);
   Class<? extends Annotation> getVariableScope(Out out);
   WrapperType wrap(Out out, Type value);
   Type getWrappedData(Out out, WrapperType wrapper);
   Object getSelection(Out out, WrapperType wrapper);
   boolean isDirty(Out out, WrapperType wrapper, Type value);
}
