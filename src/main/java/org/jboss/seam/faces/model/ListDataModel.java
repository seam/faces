//$Id: ListDataModel.java 5372 2007-06-21 05:27:29Z gavin $
package org.jboss.seam.faces.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * A JSF DataModel for lists - yes, I know, JSF has one, but its not
 * serializable (go figure).
 * 
 * @author Gavin King
 */
public class ListDataModel extends javax.faces.model.ListDataModel implements Serializable
{
   private static final long serialVersionUID = 5156131434571541698L;

   public ListDataModel()
   {
   }

   public ListDataModel(List list)
   {
      super(list);
   }

   private void writeObject(ObjectOutputStream oos) throws IOException
   {
      oos.writeObject(getWrappedData());
      oos.writeInt(getRowIndex());
   }

   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
   {
      this.setWrappedData(ois.readObject());
      this.setRowIndex(ois.readInt());
   }
}
