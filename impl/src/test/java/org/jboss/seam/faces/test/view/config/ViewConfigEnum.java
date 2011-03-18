package org.jboss.seam.faces.test.view.config;

import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

@ViewConfig
public enum ViewConfigEnum
{
   @ViewPattern("/*")
   @Icon("default.gif")
   DEFAULT,
   @ViewPattern("/happy/*")
   @Icon("happy.gif")
   HAPPY,
   @ViewPattern("/sad/*")
   @Icon("sad.gif")
   SAD,
   @ViewPattern("/happy/done.xhtml")
   @Icon("finished.gif")
   HAPPY_DONE;
}
