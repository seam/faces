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
package org.jboss.seam.faces.event;

import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.cdi.BeanManagerAware;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.application.MockApplication;
import org.jboss.test.faces.mock.component.MockUIComponent;
import org.jboss.test.faces.mock.component.MockUIComponentBase;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Nicklas Karlsson
 * 
 */
@RunWith(Arquillian.class)
public class SystemEventBridgeTest
{

   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(SystemEventObserver.class, SystemEventBridge.class, BeanManagerAware.class).addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject
   SystemEventBridge systemEventListener;

   private final MockFacesContext facesContext = new MockFacesContext();
   private final MockApplication application = new MockApplication();

// Skip until we find out how to set ID:s on mocks...   
// @Test
   public void testSpecificPostComponentValidation()
   {
      UIComponent c = new MockUIComponent();
      System.out.println(c.getId());
      systemEventListener.processEvent(new PostValidateEvent(c));
      assert SystemEventObserver.specificComponentValidationEvent;
   }

   @Test
   public void testExceptionQueuedEventObserver()
   {
      ExceptionQueuedEventContext eqec = new ExceptionQueuedEventContext(facesContext, new NullPointerException());
      ExceptionQueuedEvent eqe = new ExceptionQueuedEvent(eqec);
      systemEventListener.processEvent(eqe);
      assert SystemEventObserver.excecptionQueuedEvent;
   }

   @Test
   public void testPostConstructApplicationEventObserver()
   {
      systemEventListener.processEvent(new PostConstructApplicationEvent(application));
      assert SystemEventObserver.postConstructApplicationEvent;
   }

   @Test
   public void testPostConstructCustomScopeEvent()
   {
      ScopeContext sc = new ScopeContext("dummyscope", new HashMap<String, Object>());
      systemEventListener.processEvent(new PostConstructCustomScopeEvent(sc));
      assert SystemEventObserver.postConstructCustomScopeEvent;
   }

   @Test
   public void testPreDestroyApplicationEventObserver()
   {
      systemEventListener.processEvent(new PreDestroyApplicationEvent(application));
      assert SystemEventObserver.preDestroyApplicationEvent;
   }

   @Test
   public void testPreDestroyCustomScopeEventObserver()
   {
      ScopeContext sc = new ScopeContext("dummyscope", new HashMap<String, Object>());
      systemEventListener.processEvent(new PreDestroyCustomScopeEvent(sc));
      assert SystemEventObserver.preDestroyCustomScopeEvent;
   }

}
