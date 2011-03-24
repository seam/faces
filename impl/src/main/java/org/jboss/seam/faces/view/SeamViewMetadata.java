package org.jboss.seam.faces.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewMetadata;

/**
 * Wrap the built-in {@link ViewMetadata} implementation for the purpose of adding a UIViewParameter placeholder component if
 * necessary.
 * 
 * <p>
 * In JSF 2.0, the view metadata facet was introduced for the purpose of providing front-controller behavior in JSF. On an
 * initial request, the full JSF lifecycle is executed on a partial view consisting of the components in the view metadata
 * facet. However, there is one big caveat! The lifecycle is only executed if the view metadata contains at least one
 * UIViewParameter.
 * </p>
 * 
 * <p>
 * This wrapper intercepts the call to {@link ViewMetadata#createMetadataView(FacesContext)} and adds a placeholder
 * UIViewParameter if the view metadata is non-empty and there are no other UIViewParameter components.
 * </p>
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class SeamViewMetadata extends ViewMetadata {
    private ViewMetadata delegate;

    public SeamViewMetadata(ViewMetadata delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getViewId() {
        return delegate.getViewId();
    }

    @Override
    public UIViewRoot createMetadataView(FacesContext context) {
        UIViewRoot viewRoot = null;

        try {
            viewRoot = delegate.createMetadataView(context);
        } catch (RuntimeException e) {
            // deal with swallowed exception in Mojarra
            Logger.getLogger(ViewMetadata.class.getName()).log(Level.SEVERE, e.getMessage());
            throw e;
        }

        UIComponent metadataFacet = viewRoot.getFacet(UIViewRoot.METADATA_FACET_NAME);

        if (metadataFacet == null) {
            return viewRoot;
        }

        boolean foundViewParam = false;
        boolean foundOther = false;

        for (UIComponent candidate : metadataFacet.getChildren()) {
            if (candidate instanceof UIViewParameter) {
                foundViewParam = true;
            } else {
                foundOther = true;
            }
        }

        if (foundOther && !foundViewParam) {
            UIViewParameter placeholder = (UIViewParameter) context.getApplication().createComponent(
                    UIViewParameter.COMPONENT_TYPE);
            placeholder.setId(viewRoot.createUniqueId());
            placeholder.setName("");
            metadataFacet.getChildren().add(0, placeholder);
        }

        return viewRoot;
    }

}
