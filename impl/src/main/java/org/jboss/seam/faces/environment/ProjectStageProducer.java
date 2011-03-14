package org.jboss.seam.faces.environment;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * <p>
 * A producer which retrieves the Project Stage for the current
 * request of the JavaServer Faces application, storing the result as a
 * dependent-scoped bean instance.
 * </p>
 * 
 * <p>
 * This producer is named, allowing the Project Stage to be accessed via EL:
 * </p>
 * 
 * <pre>
 * #{projectStage}
 * </pre>
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class ProjectStageProducer
{
   @Named
   @Produces
   public String getProjectStage(final FacesContext context)
   {
      return context.getExternalContext().getInitParameter("javax.faces.PROJECT_STAGE");
   }
}
