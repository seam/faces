package org.jboss.seam.faces.test.view.config.annotation;

import javax.enterprise.util.AnnotationLiteral;

public class IconLiteral extends AnnotationLiteral<Icon> implements Icon
{
   private final String value;

   public IconLiteral(String value)
   {
      this.value = value;
   }

   public String value()
   {
      return value;
   }

}
