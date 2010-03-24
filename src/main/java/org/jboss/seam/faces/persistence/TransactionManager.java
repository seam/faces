/**
 * 
 */
package org.jboss.seam.faces.persistence;

import javax.enterprise.event.Observes;
import javax.faces.event.PhaseEvent;

import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class TransactionManager
{

   public void beginApplicationTransaction(@Observes @Before @RestoreView final PhaseEvent event)
   {
   }

   public void endApplicationTransaction(@Observes @Before @RenderResponse final PhaseEvent event)
   {
   }

}
