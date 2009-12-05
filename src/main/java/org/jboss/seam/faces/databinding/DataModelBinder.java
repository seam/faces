package org.jboss.seam.faces.databinding;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.seam.faces.DataModels;
import org.jboss.seam.faces.annotations.DataModel;

/**
 * Exposes a List, array, Map or Set to the UI as a JSF DataModel
 * 
 * @author Gavin King
 */
public class DataModelBinder implements DataBinder<DataModel, Object, javax.faces.model.DataModel>
{
   @Inject DataModels dataModels;
   
   public String getVariableName(DataModel out)
   {
      return out.value();
   }

   public Class<? extends Annotation> getVariableScope(DataModel out)
   {
      return out.scope();
   }

   public javax.faces.model.DataModel wrap(DataModel out, Object value)
   {
      return dataModels.getDataModel(value);
   }

   public Object getWrappedData(DataModel out, javax.faces.model.DataModel wrapper)
   {
      return wrapper.getWrappedData();
   }

   public Object getSelection(DataModel out, javax.faces.model.DataModel wrapper)
   {
      if ( wrapper.getRowCount()==0 || wrapper.getRowIndex()<0 ||
           wrapper.getRowIndex()>=wrapper.getRowCount())
      {
         return null;
      }
      else
      {
         Object rowData = wrapper.getRowData();
         if (rowData instanceof Map.Entry)
         {
            return ( (Map.Entry) rowData ).getValue();
         }
         else
         {
            return rowData;
         }
      }
   }

   public boolean isDirty(DataModel out, javax.faces.model.DataModel wrapper, Object value)
   {
      return !getWrappedData(out, wrapper).equals(value);
   }
   
}
