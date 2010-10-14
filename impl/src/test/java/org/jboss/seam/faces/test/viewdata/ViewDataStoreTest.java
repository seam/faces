/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.faces.test.viewdata;

import java.util.List;

import junit.framework.Assert;

import org.jboss.seam.faces.viewdata.ViewDataStore;
import org.jboss.seam.faces.viewdata.ViewDataStoreImpl;
import org.junit.Test;

public class ViewDataStoreTest
{
   @Test
   public void testViewDataStore()
   {
      ViewDataStore store = new ViewDataStoreImpl();
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
