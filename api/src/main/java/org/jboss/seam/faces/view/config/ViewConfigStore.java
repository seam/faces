package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.List;
/**
 * stores data specific to a given view is a heiracial fashion
 * @author Stuart Douglas
 *
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
   public abstract void addData(String viewId, Annotation annotation);

   /**
    * gets the most specific data for a given viewId
    * 
    */
   public abstract <T extends Annotation> T getData(String viewId, Class<T> type);

   /**
    * gets the most specific data for the current viewId
    */
   public abstract <T extends Annotation> T getDataForCurrentViewId(Class<T> type);

   /**
    * returns all data for a given viewId, with the most specific data at the
    * start of the list
    */
   public abstract <T extends Annotation> List<T> getAllData(String viewId, Class<T> type);

   /**
    * returns all data for the current viewId, with the most specific data at
    * the start of the list
    */
   public abstract <T extends Annotation> List<T> getAllDataForCurrentViewId(Class<T> type);

}