package org.jboss.seam.faces.lifecycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

/**
 * An abstract base class for processors for UI components in the metadata facet
 * of the UIViewRoot. This class provides infrastructure for extracting UI
 * components from the metadata facet branch of the component tree.
 * 
 * @author Dan Allen
 */
public abstract class AbstractViewMetadataProcesssor implements FacesSystemEventProcessor
{
   public abstract boolean execute();
   
   protected <C extends UIComponent> Collection<C> collectMetadataComponents(UIViewRoot viewRoot, UIComponentFilter<C> componentFilter)
   {
      UIComponent metadataFacet = viewRoot.getFacet(UIViewRoot.METADATA_FACET_NAME);
      
      if (metadataFacet == null)
      {
         return Collections.<C>emptyList();
      }
      
      Collection<C> matches = new ArrayList<C>();
      for (UIComponent candidate : metadataFacet.getChildren())
      {
         if (componentFilter.accepts(candidate))
         {
            matches.add((C) candidate);
         }
      }
      
      return matches;
   }
   
   protected abstract class UIComponentFilter<C extends UIComponent>
   {
      public abstract boolean accepts(UIComponent candidate);
   }
}
