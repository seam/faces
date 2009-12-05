//$Id: MapDataModel.java 7579 2008-03-14 12:08:54Z pete.muir@jboss.org $
package org.jboss.seam.faces.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;

/**
 * A JSF DataModel for maps.
 * 
 * @author Gavin King
 */
public class MapDataModel extends javax.faces.model.DataModel implements
   Serializable
{
   private static final long serialVersionUID = -4888962547222002402L;
   private int rowIndex = -1;
   private Map data;
   private transient List<Map.Entry> entries;

   public MapDataModel()
   {
   }

   public MapDataModel(Map map)
   {
      if (map == null)
      {
         throw new IllegalArgumentException("null map data");
      }
      setWrappedData(map);
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

   /**
    * Returns a Map.Entry
    */
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
      return new AbstractMap()
      {
         @Override
         public Set entrySet()
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
      this.data = (Map) data;
      entries = data == null ? null : new ArrayList(((Map) data).entrySet());
      setRowIndex(data != null ? 0 : -1);
   }

   private void writeObject(ObjectOutputStream oos) throws IOException
   {
      oos.writeInt(rowIndex);
      oos.writeObject(data);
   }

   private void readObject(ObjectInputStream ois) throws IOException,
      ClassNotFoundException
   {
      rowIndex = ois.readInt();
      data = (Map) ois.readObject();
      entries = data == null ? null : new ArrayList(data.entrySet());
   }
}
