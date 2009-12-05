package org.jboss.seam.faces.databinding;

import java.util.Map;

import javax.faces.model.DataModel;

import org.jboss.seam.faces.annotations.DataModelSelectionIndex;

/**
 * Extracts the selected "index" (the row index, or the key of a map) from a JSF DataModel.
 * 
 * @author Gavin King
 */
public class DataModelIndexSelector implements DataSelector<DataModelSelectionIndex, DataModel>
{

   public String getVariableName(DataModelSelectionIndex in)
   {
      return in.value();
   }

   public Object getSelection(DataModelSelectionIndex in, DataModel wrapper)
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
            return ( (Map.Entry) rowData ).getKey();
         }
         else
         {
            return wrapper.getRowIndex();
         }
      }
   }
   
}
