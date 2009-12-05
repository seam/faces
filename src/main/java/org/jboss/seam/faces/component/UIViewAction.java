package org.jboss.seam.faces.component;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;

/**
 * TODO add conditional (if attribute)
 * 
 * @author Dan Allen
 */
public class UIViewAction extends UIComponentBase
{

   // ------------------------------------------------------ Manifest Constants

   /**
    * <p>
    * The standard component type for this component.
    * </p>
    */
   public static final String COMPONENT_TYPE = "org.jboss.seam.faces.ViewAction";

   /**
    * <p>
    * The standard component type for this component.
    * </p>
    */
   public static final String COMPONENT_FAMILY = "org.jboss.seam.faces.ViewAction";
   
   /**
    * Properties that are tracked by state saving.
    */
   enum PropertyKeys
   {
      onPostback, execute, ifAttr("if");

      private String name;

      PropertyKeys()
      {
      }

      PropertyKeys(String name)
      {
         this.name = name;
      }

      @Override
      public String toString()
      {
         return name != null ? name : super.toString();
      }
   }
   
   // ------------------------------------------------------------ Constructors

   /**
    * <p>
    * Create a new {@link UIViewAction} instance with default property values.
    * </p>
    */
   public UIViewAction()
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

   public MethodExpression getExecute()
   {
      return (MethodExpression) getStateHelper().get(PropertyKeys.execute);
   }
   
   public void setExecute(MethodExpression execute)
   {
      getStateHelper().put(PropertyKeys.execute, execute);
   }

   public boolean isOnPostback()
   {
      return (Boolean) getStateHelper().eval(PropertyKeys.onPostback, false);
   }

   public void setOnPostback(boolean onPostback)
   {
      getStateHelper().put(PropertyKeys.onPostback, onPostback);
   }
   
   public boolean isIf()
   {
      return (Boolean) getStateHelper().eval(PropertyKeys.ifAttr, true);
   }

   public void setIf(boolean condition)
   {
      getStateHelper().put(PropertyKeys.ifAttr, condition);
   }

}
