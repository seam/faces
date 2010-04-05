package org.jboss.seam.faces.environment;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;

/**
 * @author Dan Allen
 */
public class MockExternalContext extends ExternalContextWrapper
{
   @Override
   public String getRequestContextPath()
   {
      return "/app";
   }

   @Override
   public ExternalContext getWrapped()
   {
      throw new UnsupportedOperationException("Not supported.");
   }
}
