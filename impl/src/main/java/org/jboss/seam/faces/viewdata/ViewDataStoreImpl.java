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
package org.jboss.seam.faces.viewdata;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * Data store for view specific data.
 * 
 * @author Stuart Douglas
 * 
 */
@ApplicationScoped
public class ViewDataStoreImpl implements ViewDataStore
{
   /**
    * cache of viewId to a given data list
    */
   private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> cache = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>>();
   
   private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>> data = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>>();

   /**
    * setup the bean with the configuration from the extension
    * 
    * It would be better if the extension could do this, but the extension
    * cannot resolve the bean until after all lifecycle events have been
    * processed
    * 
    */
   @Inject
   public void setup(ViewDataConfigurationExtension extension)
   {
      for (Entry<String, Set<Annotation>> e : extension.getData().entrySet())
      {
         for (Annotation i : e.getValue())
         {
            addData(e.getKey(), i);
         }
      }
   }

   public synchronized void addData(String viewId, Annotation annotation)
   {
      ConcurrentHashMap<String, Annotation> map = data.get(annotation.annotationType());
      if (map == null)
      {
         map = new ConcurrentHashMap<String, Annotation>();
         data.put(annotation.annotationType(), map);
      }
      map.put(viewId, annotation);
   }

   public <T extends Annotation> T getData(String viewId, Class<T> type)
   {
      List<T> data = prepareCache(viewId, type);
      if (data != null)
      {
         return data.get(0);
      }
      return null;
   }

   public <T extends Annotation> T getDataForCurrentViewId(Class<T> type)
   {
      return getData(FacesContext.getCurrentInstance().getViewRoot().getViewId(), type);
   }

   public <T extends Annotation> List<T> getAllData(String viewId, Class<T> type)
   {
      List<T> data = prepareCache(viewId, type);
      if (data != null)
      {
         return Collections.unmodifiableList(data);
      }
      return null;
   }

   public <T extends Annotation> List<T> getAllDataForCurrentViewId(Class<T> type)
   {
      return getAllData(FacesContext.getCurrentInstance().getViewRoot().getViewId(), type);
   }

   private <T extends Annotation> List<T> prepareCache(String viewId, Class<T> type)
   {
      // we need to synchonise to make sure that no threads see a half completed
      // list due to instruction re-ordering
      ConcurrentHashMap<String, List<? extends Annotation>> map = cache.get(type);
      if (map == null)
      {
         ConcurrentHashMap<String, List<? extends Annotation>> newMap = new ConcurrentHashMap<String, List<? extends Annotation>>();
         map = cache.putIfAbsent(type, newMap);
         if (map == null)
         {
            map = newMap;
         }
      }
      List<? extends Annotation> annotationData = map.get(viewId);
      if (annotationData == null)
      {
         List<Annotation> newList = new ArrayList<Annotation>();
         Map<String, Annotation> viewData = data.get(type);
         List<String> resultingViews = new ArrayList<String>();
         if (viewData != null)
         {
            for (Entry<String, Annotation> e : viewData.entrySet())
            {
               if (e.getKey().endsWith("*"))
               {
                  String cutView = e.getKey().substring(0, e.getKey().length() - 1);
                  if (viewId.startsWith(cutView))
                  {
                     resultingViews.add(e.getKey());
                  }
               }
               else
               {
                  if (e.getKey().equals(viewId))
                  {
                     resultingViews.add(e.getKey());
                  }
               }
            }
            // sort the keys by length, longest is the most specific and so
            // should go first
            Collections.sort(resultingViews, StringLengthComparator.INSTANCE);
            for (String i : resultingViews)
            {
               newList.add(viewData.get(i));
            }
         }

         annotationData = map.putIfAbsent(viewId, newList);
         if (annotationData == null)
         {
            annotationData = newList;
         }
      }
      return (List) annotationData;
   }

   private static class StringLengthComparator implements Comparator<String>
   {

      public int compare(String o1, String o2)
      {
         if (o1.length() > o2.length())
         {
            return -1;
         }
         if (o1.length() < o2.length())
         {
            return 1;
         }
         return 0;
      }

      public static final StringLengthComparator INSTANCE = new StringLengthComparator();

   }
}
