package org.jboss.seam.faces.lifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.jboss.seam.faces.component.UIImport;

/**
 * <p>
 * A metadata facet processor that executes on a non-faces request immediately
 * prior to the view being rendered. The processor collects the EL namespaces
 * and stores them in the view-scope (the view map of the UIViewRoot) where they
 * can be consulted by the EL resolver.
 * </p>
 * 
 * <p>
 * An EL namespace is identical to a Java namespace, except for EL names. EL
 * names are fully-qualified to prevent naming conflicts, but it is convenient
 * for the page author to shorten those names by importing the namespace. When
 * the EL resolver is resolving the base object, the namespaces are prepended to
 * the name to match a fully-qualified EL name.
 * </p>
 * 
 * @author Dan Allen
 */
public class ImportNamespacesProcessor extends AbstractViewMetadataProcesssor
{
   public static final String NAMESPACES_CACHE_KEY = "org.jboss.seam.faces.NAMESPACES_CACHE";
   
   @Override
   public boolean execute()
   {
      UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
      Map<String, Object> viewMap = viewRoot.getViewMap();
      if (viewMap.containsKey(NAMESPACES_CACHE_KEY))
      {
         return true;
      }
      
      Collection<String> aggregateNamespaces = new ArrayList<String>();
      for (UIImport uiImport : collectImports(viewRoot))
      {
         Object namespaces = uiImport.getNamespaces();
         
         if (namespaces instanceof Collection)
         {
            for (Object candidate : (Collection<?>) namespaces)
            {
               if (candidate instanceof String && ((String) candidate).length() > 0)
               {
                  aggregateNamespaces.add((String) candidate);
               }
            }
         }
         else if (namespaces instanceof String)
         {
            for (String candidate : Arrays.asList(((String) namespaces).split("[\\s]*,[\\s]*")))
            {
               if (candidate.length() > 0)
               {
                  aggregateNamespaces.add(candidate);
               }
            }
         }
      }
      
      viewMap.put(NAMESPACES_CACHE_KEY, aggregateNamespaces);
      return true;
   }
   
   /**
    * Pick out the UIImport components from the metadata facet's children.
    */
   protected Collection<UIImport> collectImports(UIViewRoot viewRoot)
   {
      return collectMetadataComponents(viewRoot, new UIComponentFilter<UIImport>() {

         @Override
         public boolean accepts(UIComponent candidate)
         {
            return candidate instanceof UIImport;
         }
         
      });
   }
}
