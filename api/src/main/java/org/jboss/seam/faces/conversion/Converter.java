package org.jboss.seam.faces.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p>
 * A generic abstract class implementing Converter, for convenient removal of type casting.
 * </p>
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public abstract class Converter<T> implements javax.faces.convert.Converter {

    private FacesContext context;

    public abstract T toObject(UIComponent comp, String value);

    public abstract String toString(UIComponent comp, T value);

    @Override
    public Object getAsObject(final FacesContext context, final UIComponent comp, final String value) {
        this.context = context;
        return toObject(comp, value);
    }

    @Override
    public String getAsString(final FacesContext context, final UIComponent comp, final Object value) {
        this.context = context;
        return toString(comp, (T) value);
    }

    public FacesContext getContext() {
        return context;
    }
}