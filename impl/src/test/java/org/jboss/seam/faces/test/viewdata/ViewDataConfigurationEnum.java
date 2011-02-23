package org.jboss.seam.faces.test.viewdata;

import org.jboss.seam.faces.viewdata.ViewConfig;
import org.jboss.seam.faces.viewdata.ViewData;

@ViewConfig
public enum ViewDataConfigurationEnum
{
   @ViewData("/*")
   @Icon("default.gif")
   DEFAULT,
   @ViewData("/happy/*")
   @Icon("happy.gif")
   HAPPY,
   @ViewData("/sad/*")
   @Icon("sad.gif")
   SAD,
   @ViewData("/happy/done.xhtml")
   @Icon("finished.gif")
   HAPPY_DONE;
}
