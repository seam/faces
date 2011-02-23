package org.jboss.seam.faces.test.component;

import javax.faces.component.UIViewRoot;

import org.jboss.seam.faces.component.UIValidateForm;
import org.jboss.test.faces.mock.application.MockApplication;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class UIValidateFormTest
{

   private final MockFacesContext facesContext = new MockFacesContext();
   private final MockApplication application = new MockApplication();
   private static final UIViewRoot uiViewRoot = new UIViewRoot();

   static
   {
      uiViewRoot.setViewId("foo.xhtml");
   }

   @Test
   public void testCanLocateChildComponents() throws Exception
   {
      UIValidateForm vf = new UIValidateForm();
   }
}
