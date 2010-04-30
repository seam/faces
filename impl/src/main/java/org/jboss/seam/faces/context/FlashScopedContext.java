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
package org.jboss.seam.faces.context;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.faces.bean.FlashScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.event.PreRenderViewEvent;
import javax.inject.Inject;
import javax.servlet.ServletRequest;

/**
 * This class provides the lifecycle for the new JSF 2 Flash Context
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FlashScopedContext implements Context, PhaseListener
{
   private static final long serialVersionUID = -1580689204988513798L;

   final static String COMPONENT_MAP_NAME = "org.jboss.seam.faces.flash.componentInstanceMap";
   final static String CREATIONAL_MAP_NAME = "org.jboss.seam.faces.flash.creationalInstanceMap";

   @SuppressWarnings("unchecked")
   public <T> T get(final Contextual<T> component)
   {
      assertActive();
      return (T) getComponentInstanceMap().get(component);
   }

   @SuppressWarnings("unchecked")
   public <T> T get(final Contextual<T> component, final CreationalContext<T> creationalContext)
   {
      assertActive();

      T instance = get(component);

      if (instance == null)
      {
         Map<Contextual<?>, CreationalContext<?>> creationalContextMap = getCreationalContextMap();
         Map<Contextual<?>, Object> componentInstanceMap = getComponentInstanceMap();

         synchronized (componentInstanceMap)
         {
            instance = (T) componentInstanceMap.get(component);
            if (instance == null)
            {
               instance = component.create(creationalContext);

               if (instance != null)
               {
                  componentInstanceMap.put(component, instance);
                  creationalContextMap.put(component, creationalContext);
               }
            }
         }
      }

      return instance;
   }

   public Class<? extends Annotation> getScope()
   {
      return FlashScoped.class;
   }

   public boolean isActive()
   {
      return getFlash() != null;
   }

   @Inject
   FacesContext context;

   /**
    * This method ensures that the contextual maps are populated before
    * rendering occurs (thus, before any contextual objects are created during
    * the Render Response phase.)
    * <p>
    * This method also ensures that the maps are available after Flash.clear()
    * is called immediately before Render Response is complete.
    * 
    * @param event
    * @throws IOException
    */
   public void retrieveContextualMaps(@Observes final PreRenderViewEvent event) throws IOException
   {
      ExternalContext externalContext = context.getExternalContext();
      Object temp = externalContext.getRequest();
      if (temp instanceof ServletRequest)
      {
         Object componentMap = getComponentInstanceMap();
         Object creationalMap = getCreationalContextMap();

         ServletRequest request = (ServletRequest) temp;
         request.setAttribute(FlashScopedContext.COMPONENT_MAP_NAME, componentMap);
         request.setAttribute(FlashScopedContext.CREATIONAL_MAP_NAME, creationalMap);
      }
   }

   public void beforePhase(final PhaseEvent event)
   {
   }

   /**
    * This method saves the current scope metadata into the Flash after Restore
    * View, then destroys the metadata after Render Response. Since the current
    * request's execution Flash is swapped with the last requests execution
    * flash for the Render Response phase, the last request's execution flash is
    * actually the one that gets cleaned up.
    * <p>
    * Preserve this request's new metadata. Do the object cleanup using our
    * saved references from last request.
    */
   @SuppressWarnings("unchecked")
   public void afterPhase(final PhaseEvent event)
   {
      if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId()))
      {
         ExternalContext externalContext = event.getFacesContext().getExternalContext();
         Object temp = externalContext.getRequest();
         if (temp instanceof ServletRequest)
         {
            ServletRequest request = (ServletRequest) temp;

            Map<Contextual<?>, Object> componentInstanceMap = (Map<Contextual<?>, Object>) request.getAttribute(FlashScopedContext.COMPONENT_MAP_NAME);
            Map<Contextual<?>, CreationalContext<?>> creationalContextMap = (Map<Contextual<?>, CreationalContext<?>>) request.getAttribute(FlashScopedContext.CREATIONAL_MAP_NAME);

            if ((componentInstanceMap != null) && (creationalContextMap != null))
            {
               for (Entry<Contextual<?>, Object> componentEntry : componentInstanceMap.entrySet())
               {
                  Contextual contextual = componentEntry.getKey();
                  Object instance = componentEntry.getValue();
                  CreationalContext creational = creationalContextMap.get(contextual);

                  contextual.destroy(instance, creational);
               }
            }
         }
      }
   }

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

   private Flash getFlash()
   {
      FacesContext currentInstance = FacesContext.getCurrentInstance();
      if (currentInstance != null)
      {
         ExternalContext externalContext = currentInstance.getExternalContext();
         return externalContext.getFlash();
      }
      return null;
   }

   private void assertActive()
   {
      if (!isActive())
      {
         throw new ContextNotActiveException("Seam context with scope annotation @FlashScoped is not active with respect to the current thread");
      }
   }

   @SuppressWarnings("unchecked")
   private Map<Contextual<?>, Object> getComponentInstanceMap()
   {
      Flash flash = getFlash();
      ConcurrentHashMap<Contextual<?>, Object> map = (ConcurrentHashMap<Contextual<?>, Object>) flash.get(COMPONENT_MAP_NAME);

      if (map == null)
      {
         map = new ConcurrentHashMap<Contextual<?>, Object>();
         flash.put(COMPONENT_MAP_NAME, map);
      }

      return map;
   }

   @SuppressWarnings("unchecked")
   private Map<Contextual<?>, CreationalContext<?>> getCreationalContextMap()
   {
      Flash flash = getFlash();
      Map<Contextual<?>, CreationalContext<?>> map = (ConcurrentHashMap<Contextual<?>, CreationalContext<?>>) flash.get(CREATIONAL_MAP_NAME);

      if (map == null)
      {
         map = new ConcurrentHashMap<Contextual<?>, CreationalContext<?>>();
         flash.put(CREATIONAL_MAP_NAME, map);
      }

      return map;
   }

}
