package org.jboss.seam.faces.test.view.config;

import java.lang.annotation.Annotation;
import java.util.List;

import junit.framework.Assert;

import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.faces.view.config.ViewConfigStoreImpl;
import org.junit.Ignore;
import org.junit.Test;

public class ViewConfigStoreTest
{
   @Test
   public void testViewConfigStore()
   {
      ViewConfigStore store = new ViewConfigStoreImpl();
      store.addAnnotationData("/*", new IconLiteral("default.gif"));
      store.addAnnotationData("/sad/*", new IconLiteral("sad.gif"));
      store.addAnnotationData("/happy/*", new IconLiteral("happy.gif"));
      store.addAnnotationData("/happy/done.xhtml", new IconLiteral("finished.gif"));
      store.addAnnotationData("/qualified/yes.xhtml", new QualifiedIconLiteral("qualified.gif"));

      Icon data;
      data = store.getAnnotationData("/happy/done.xhtml", Icon.class);
      Assert.assertEquals("finished.gif", data.value());
      data = store.getAnnotationData("/happy/other.xhtml", Icon.class);
      Assert.assertEquals("happy.gif", data.value());
      data = store.getAnnotationData("/default/news.xhtml", Icon.class);
      Assert.assertEquals("default.gif", data.value());
      QualifiedIcon qualifiedData;
      qualifiedData = store.getAnnotationData("/qualified/yes.xhtml", QualifiedIcon.class);
      Assert.assertEquals("qualified.gif", qualifiedData.value());

      List<Icon> dlist;
      dlist = store.getAllAnnotationData("/happy/done.xhtml", Icon.class);
      Assert.assertEquals(3, dlist.size());
      Assert.assertEquals("finished.gif", dlist.get(0).value());
      Assert.assertEquals("happy.gif", dlist.get(1).value());
      Assert.assertEquals("default.gif", dlist.get(2).value());
      dlist = store.getAllAnnotationData("/happy/other.xhtml", Icon.class);
      Assert.assertEquals(2, dlist.size());
      Assert.assertEquals("happy.gif", dlist.get(0).value());
      Assert.assertEquals("default.gif", dlist.get(1).value());
      dlist = store.getAllAnnotationData("/default/news.xhtml", Icon.class);
      Assert.assertEquals(1, dlist.size());
      Assert.assertEquals("default.gif", dlist.get(0).value());
   }
   
   @Test
   @Ignore
   public void testViewConfigStoreQualified()
   {
      ViewConfigStore store = new ViewConfigStoreImpl();
      store.addAnnotationData("/*", new IconLiteral("default.gif"));
      store.addAnnotationData("/sad/*", new IconLiteral("sad.gif"));
      store.addAnnotationData("/happy/*", new IconLiteral("happy.gif"));
      store.addAnnotationData("/happy/done.xhtml", new IconLiteral("finished.gif"));
      store.addAnnotationData("/qualified/yes.xhtml", new QualifiedIconLiteral("qualified.gif"));
      
      List<? extends Annotation> qdlist;
      qdlist = store.getAllQualifierData("/qualified/yes.xhtml", TestQualifier.class);
      Assert.assertEquals(1, qdlist.size());
   }
}
