package org.jboss.seam.faces.component;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;

/**
 * 
 * TODO add login-required attribute. If require fails, we only force them to login
 * if that helps them get to the page.
 * 
 * @author Dan Allen
 */
public class UIRestrictView extends UIComponentBase
{

   // ------------------------------------------------------ Manifest Constants

   /**
    * <p>
    * The standard component type for this component.
    * </p>
    */
   public static final String COMPONENT_TYPE = "org.jboss.seam.faces.RestrictView";

   /**
    * <p>
    * The standard component type for this component.
    * </p>
    */
   public static final String COMPONENT_FAMILY = "org.jboss.seam.faces.RestrictView";
   
   /**
    * Properties that are tracked by state saving.
    */
   enum PropertyKeys {
       require
   }
   
   // ------------------------------------------------------------ Constructors

   /**
    * <p>
    * Create a new {@link UIRestrictView} instance with default property values.
    * </p>
    */
   public UIRestrictView()
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

   public ValueExpression getRequire()
   {
      return (ValueExpression) getStateHelper().get(PropertyKeys.require);
   }
   
   public void setRequire(ValueExpression require)
   {
      getStateHelper().put(PropertyKeys.require, require);
   }
}
