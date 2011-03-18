package org.jboss.seam.faces.test.view.config;

import java.util.List;

import junit.framework.Assert;

import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.junit.Test;

public class ViewConfigStoreTest
{
   @Test
   public void testViewConfigStore()
   {
      ViewConfigStore store = new ViewConfigStoreImpl();
      store.addData("/*", new IconLiteral("default.gif"));
      store.addData("/sad/*", new IconLiteral("sad.gif"));
      store.addData("/happy/*", new IconLiteral("happy.gif"));
      store.addData("/happy/done.xhtml", new IconLiteral("finished.gif"));

      Icon data;
      data = store.getData("/happy/done.xhtml", Icon.class);
      Assert.assertEquals("finished.gif", data.value());
      data = store.getData("/happy/other.xhtml", Icon.class);
      Assert.assertEquals("happy.gif", data.value());
      data = store.getData("/default/news.xhtml", Icon.class);
      Assert.assertEquals("default.gif", data.value());

      List<Icon> dlist;
      dlist = store.getAllData("/happy/done.xhtml", Icon.class);
      Assert.assertEquals(3, dlist.size());
      Assert.assertEquals("finished.gif", dlist.get(0).value());
      Assert.assertEquals("happy.gif", dlist.get(1).value());
      Assert.assertEquals("default.gif", dlist.get(2).value());
      dlist = store.getAllData("/happy/other.xhtml", Icon.class);
      Assert.assertEquals(2, dlist.size());
      Assert.assertEquals("happy.gif", dlist.get(0).value());
      Assert.assertEquals("default.gif", dlist.get(1).value());
      dlist = store.getAllData("/default/news.xhtml", Icon.class);
      Assert.assertEquals(1, dlist.size());
      Assert.assertEquals("default.gif", dlist.get(0).value());

   }
}
