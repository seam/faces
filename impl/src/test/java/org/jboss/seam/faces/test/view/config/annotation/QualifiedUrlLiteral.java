package org.jboss.seam.faces.test.view.config.annotation;

import javax.enterprise.util.AnnotationLiteral;

public class QualifiedUrlLiteral extends AnnotationLiteral<QualifiedUrl> implements QualifiedUrl
{
   private final String value;

   public QualifiedUrlLiteral(String value)
   {
      this.value = value;
   }

   public String value()
   {
      return value;
   }

}
