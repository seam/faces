package org.jboss.seam.faces.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;

import org.jboss.seam.solder.el.Resolver;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Resolver
public class FacesContextELResolver extends ELResolver {
    private ELResolver getWrapped() {
        return FacesContext.getCurrentInstance().getELContext().getELResolver();
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        return getWrapped().getValue(context, base, property);
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return getWrapped().getType(context, base, property);
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        getWrapped().setValue(context, base, property, value);
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return getWrapped().isReadOnly(context, base, property);
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return getWrapped().getFeatureDescriptors(context, base);
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return getWrapped().getCommonPropertyType(context, base);
    }

}
