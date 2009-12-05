package org.jboss.seam.faces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModel;

import org.jboss.seam.faces.model.ArrayDataModel;
import org.jboss.seam.faces.model.ListDataModel;
import org.jboss.seam.faces.model.MapDataModel;
import org.jboss.seam.faces.model.SetDataModel;

//import org.jboss.seam.framework.Query;
/**
 * Wraps a collection as a JSF {@link DataModel}. May be overridden
 * and extended if you don't like the built in collections
 * which are supported: list, map, set, array.
 *
 * @author pmuir
 */
public class DataModels
{
   /**
    * Wrap the value in a DataModel
    * 
    * This implementation supports {@link List}, {@link Map}, {@link Set} and
    * arrays
    */
   public DataModel getDataModel(Object value)
   {
      if (value instanceof List)
      {
         return new ListDataModel((List) value);
      }
      else if (value instanceof Object[])
      {
         return new ArrayDataModel((Object[]) value);
      }
      else if (value instanceof Map)
      {
         return new MapDataModel((Map) value);
      }
      else if (value instanceof Set)
      {
         return new SetDataModel((Set) value);
      }
      else
      {
         throw new IllegalArgumentException("unknown collection type: " + value.getClass());
      }
   }
   /**
    * Wrap the the Seam Framework {@link Query} in a JSF DataModel
    */
//   public DataModel getDataModel(Query query)
//   {
//      return getDataModel( query.getResultList() );
//   }
}
