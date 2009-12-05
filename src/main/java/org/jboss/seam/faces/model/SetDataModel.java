//$Id: SetDataModel.java 7577 2008-03-14 11:47:09Z pete.muir@jboss.org $
package org.jboss.seam.faces.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;

/**
 * A JSF DataModel for sets.
 * 
 * @author Gavin King
 */
public class SetDataModel extends javax.faces.model.DataModel implements
   Serializable
{
   private static final long serialVersionUID = -616367764778689337L;
   private int rowIndex = -1;
   private List entries;

   public SetDataModel()
   {
   }

   public SetDataModel(Set set)
   {
      if (set == null)
      {
         throw new IllegalArgumentException("null set data");
      }
      setWrappedData(set);
   }

   @Override
   public int getRowCount()
   {
      if (entries == null)
      {
         return -1;
      }
      return entries.size();
   }

   @Override
   public Object getRowData()
   {
      if (entries == null)
      {
         return null;
      }
      if (!isRowAvailable())
      {
         throw new IllegalArgumentException("row is unavailable");
      }
      return entries.get(rowIndex);
   }

   @Override
   public int getRowIndex()
   {
      return rowIndex;
   }

   @Override
   public Object getWrappedData()
   {
      return new AbstractSet()
      {
         @Override
         public Iterator iterator()
         {
            return entries.iterator();
         }

         @Override
         public int size()
         {
            return entries.size();
         }
      };
   }

   @Override
   public boolean isRowAvailable()
   {
      return entries != null &&
         rowIndex >= 0 &&
         rowIndex < entries.size();
   }

   @Override
   public void setRowIndex(int newRowIndex)
   {
      if (newRowIndex < -1)
      {
         throw new IllegalArgumentException("illegal rowIndex " + newRowIndex);
      }
      int oldRowIndex = rowIndex;
      rowIndex = newRowIndex;
      if (entries != null && oldRowIndex != newRowIndex)
      {
         Object data = isRowAvailable() ? getRowData() : null;
         DataModelEvent event = new DataModelEvent(this, newRowIndex, data);
         DataModelListener[] listeners = getDataModelListeners();
         for (int i = 0; i < listeners.length; i++)
         {
            listeners[i].rowSelected(event);
         }
      }
   }

   @Override
   public void setWrappedData(Object data)
   {
      entries = data == null ? null : new ArrayList((Set) data);
      setRowIndex(data != null ? 0 : -1);
   }

   private void writeObject(ObjectOutputStream oos) throws IOException
   {
      oos.writeInt(rowIndex);
      oos.writeObject(entries);
   }

   private void readObject(ObjectInputStream ois) throws IOException,
      ClassNotFoundException
   {
      rowIndex = ois.readInt();
      entries = (List) ois.readObject();
   }
}
