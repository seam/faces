package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.List;
/**
 * stores data specific to a given view is a hierarchical fashion
 * 
 * @author Stuart Douglas
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public interface ViewConfigStore
{

   /**
    * Adds data to the store
    * 
    * @param viewId The view id to associate the data with. A * at the end of
    *           the view id is considered a wildcard
    * @param annotation the data to store
    */
   public abstract void addAnnotationData(String viewId, Annotation annotation);

   /**
    * gets the most specific data for a given viewId
    * 
    */
   public abstract <T extends Annotation> T getAnnotationData(String viewId, Class<T> type);

   /**
    * returns all data for a given viewId, with the most specific data at the
    * start of the list
    */
   public abstract <T extends Annotation> List<T> getAllAnnotationData(String viewId, Class<T> type);

   /**
    * returns all qualified data for a given viewId, with the most specific data at the
    * start of the list
    */
   public abstract List<? extends Annotation> getAllQualifierData(String viewId, Class<? extends Annotation> qualifier);

}