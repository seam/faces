package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Data store for view specific data.
 * 
 * @author Stuart Douglas
 * 
 */
@ApplicationScoped
public class ViewConfigStoreImpl implements ViewConfigStore
{
   /**
    * cache of viewId to a given data list
    */
   private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> annotationCache = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>>();
   private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> qualifierCache = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>>();

   private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>> viewPatternDataByAnnotation = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>>();
   private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>> viewPatternDataByQualifier = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>>();

   /**
    * setup the bean with the configuration from the extension
    * 
    * It would be better if the extension could do this, but the extension
    * cannot resolve the bean until after all lifecycle events have been
    * processed
    * 
    */
   @Inject
   public void setup(ViewConfigExtension extension)
   {
      for (Entry<String, Set<Annotation>> e : extension.getData().entrySet())
      {
         for (Annotation i : e.getValue())
         {
            addAnnotationData(e.getKey(), i);
         }
      }
   }

   @Override
   public synchronized void addAnnotationData(String viewId, Annotation annotation)
   {
      ConcurrentHashMap<String, Annotation> annotationMap = viewPatternDataByAnnotation.get(annotation.annotationType());
      if (annotationMap == null)
      {
         annotationMap = new ConcurrentHashMap<String, Annotation>();
         viewPatternDataByAnnotation.put(annotation.annotationType(), annotationMap);
      }
      annotationMap.put(viewId, annotation);

      Annotation[] annotations = annotation.getClass().getAnnotations();
      for (Annotation qualifier : annotations)
      {
         if (qualifier.getClass().getName().startsWith("java."))
         {
             continue;
         }
         ConcurrentHashMap<String, Annotation> qualifierMap = viewPatternDataByQualifier.get(qualifier.annotationType());
         if (qualifierMap == null)
         {
            qualifierMap = new ConcurrentHashMap<String, Annotation>();
            viewPatternDataByQualifier.put(qualifier.annotationType(), qualifierMap);
         }
         qualifierMap.put(viewId, qualifier);
      }
   }

    @Override
   public <T extends Annotation> T getAnnotationData(String viewId, Class<T> type)
   {
      List<T> data = prepareCache(viewId, type, annotationCache, viewPatternDataByAnnotation);
      if ((data != null) && (data.size() > 0))
      {
         return data.get(0);
      }
      return null;
   }

   @Override
   public <T extends Annotation> List<T> getAllAnnotationData(String viewId, Class<T> type)
   {
      List<T> data = prepareCache(viewId, type, annotationCache, viewPatternDataByAnnotation);
      if (data != null)
      {
         return Collections.unmodifiableList(data);
      }
      return null;
   }

    @Override
    public List<? extends Annotation> getAllQualifierData(String viewId, Class<? extends Annotation> qualifier) {
        List<? extends Annotation> data = prepareCache(viewId, qualifier, qualifierCache, viewPatternDataByQualifier);
      if (data != null)
      {
         return Collections.unmodifiableList(data);
      }
      return null;
    }

   private <T extends Annotation> List<T> prepareCache(String viewId, Class<T> annotationType,
           ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> cache,
           ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>> viewPatternData)
   {
      // we need to synchronize to make sure that no threads see a half 
      // completed list due to instruction re-ordering
      ConcurrentHashMap<String, List<? extends Annotation>> map = cache.get(annotationType);
      if (map == null)
      {
         ConcurrentHashMap<String, List<? extends Annotation>> newMap = new ConcurrentHashMap<String, List<? extends Annotation>>();
         map = cache.putIfAbsent(annotationType, newMap);
         if (map == null)
         {
            map = newMap;
         }
      }
      List<? extends Annotation> annotationData = map.get(viewId);
      if (annotationData == null)
      {
         List<Annotation> newList = new ArrayList<Annotation>();
         Map<String, Annotation> viewPatterns = viewPatternData.get(annotationType);
         if (viewPatterns != null)
         {
            List<String> resultingViews = findViewsWithPatternsThatMatch(viewId, viewPatterns);
            for (String i : resultingViews)
            {
               newList.add(viewPatterns.get(i));
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
   
   private List<String> findViewsWithPatternsThatMatch(String viewId, Map<String, Annotation> viewPatterns)
   {
      List<String> resultingViews = new ArrayList<String>();
      for (Entry<String, Annotation> e : viewPatterns.entrySet())
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
      // sort the keys by length, longest is the most specific and so should go first
      Collections.sort(resultingViews, StringLengthComparator.INSTANCE);
      return resultingViews;
   }

   private static class StringLengthComparator implements Comparator<String>
   {

      @Override
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
