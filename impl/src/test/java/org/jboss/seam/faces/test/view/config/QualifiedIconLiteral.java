package org.jboss.seam.faces.test.view.config;

import javax.enterprise.util.AnnotationLiteral;

public class QualifiedIconLiteral extends AnnotationLiteral<QualifiedIcon> implements QualifiedIcon
{
   private final String value;

   public QualifiedIconLiteral(String value)
   {
      this.value = value;
   }

   public String value()
   {
      return value;
   }

}
