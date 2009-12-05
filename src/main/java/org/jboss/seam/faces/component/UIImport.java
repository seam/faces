package org.jboss.seam.faces.component;

import javax.faces.component.UIComponentBase;

/**
 * @author Dan Allen
 */
public class UIImport extends UIComponentBase
{

   // ------------------------------------------------------ Manifest Constants

   /**
    * <p>
    * The standard component type for this component.
    * </p>
    */
   public static final String COMPONENT_TYPE = "org.jboss.seam.faces.Import";

   /**
    * <p>
    * The standard component type for this component.
    * </p>
    */
   public static final String COMPONENT_FAMILY = "org.jboss.seam.faces.Import";
   
   /**
    * Properties that are tracked by state saving.
    */
   enum PropertyKeys {
       namespaces
   }
   
   // ------------------------------------------------------------ Constructors

   /**
    * <p>
    * Create a new {@link UIImport} instance with default property values.
    * </p>
    */
   public UIImport()
   {
      super();
      setRendererType(null);
   }

   // -------------------------------------------------------------- Properties

   @Override
   public String getFamily()
   {
      return COMPONENT_FAMILY;
   }

   public Object getNamespaces()
   {
      return getStateHelper().eval(PropertyKeys.namespaces);
   }
   
   public void setNamespaces(Object namespaces)
   {
      getStateHelper().put(PropertyKeys.namespaces, namespaces);
   }
   
}
