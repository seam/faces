package org.jboss.seam.faces;

import java.io.IOException;

public class RedirectException extends RuntimeException
{

   public RedirectException(IOException ioe)
   {
      super(ioe);
   }
   
   public RedirectException(String message)
   {
      super(message);
   }
}
