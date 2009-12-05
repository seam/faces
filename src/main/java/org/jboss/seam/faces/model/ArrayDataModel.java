//$Id: ArrayDataModel.java 5372 2007-06-21 05:27:29Z gavin $
package org.jboss.seam.faces.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A JSF DataModel for arrays.
 * 
 * @author Gavin King
 */
public class ArrayDataModel extends javax.faces.model.ArrayDataModel implements
   Serializable
{
   private static final long serialVersionUID = -1369792328129853864L;

   public ArrayDataModel()
   {
   }

   public ArrayDataModel(Object[] array)
   {
      super(array);
   }

   private void writeObject(ObjectOutputStream oos) throws IOException
   {
      oos.writeObject(getWrappedData());
      oos.writeInt(getRowIndex());
   }

   private void readObject(ObjectInputStream ois) throws IOException,
      ClassNotFoundException
   {
      this.setWrappedData(ois.readObject());
      this.setRowIndex(ois.readInt());
   }
}
