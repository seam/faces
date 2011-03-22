package org.jboss.seam.faces.test.view.config.annotation;

import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

@ViewConfig
public interface ViewConfigEnum
{
   @ViewPattern("/*")
   @Icon("default.gif")
   void DEFAULT();
   @ViewPattern("/happy/*")
   @Icon("happy.gif")
   void HAPPY();
   @ViewPattern("/sad/*")
   @Icon("sad.gif")
   void SAD();
   @ViewPattern("/happy/done.xhtml")
   @Icon("finished.gif")
   void HAPPY_DONE();
   @ViewPattern("/qualified/*")
   @QualifiedIcon("qualified.gif")
   void QUALIFIED();
}
