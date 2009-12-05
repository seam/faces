package org.jboss.seam.faces.databinding;

import java.util.Map;

import javax.faces.model.DataModel;

import org.jboss.seam.faces.annotations.DataModelSelection;

/**
 * Extracts the selected object (the element, or the value of a map) from a JSF DataModel.
 * 
 * @author Gavin King
 */
public class DataModelSelector implements DataSelector<DataModelSelection, DataModel>
{
   
   public String getVariableName(DataModelSelection in)
   {
      return in.value();
   }

   public Object getSelection(DataModelSelection in, DataModel wrapper)
   {
      if ( wrapper.getRowCount()==0 || wrapper.getRowIndex()<0 )
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
   
}
