package org.jboss.seam.faces.environment;

import javax.enterprise.inject.Produces;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * <p>
 * A producer which retrieves the ProjectStage for the current request of the JavaServer Faces application, storing the result
 * as a ApplicationScoped bean instance.
 * </p>
 * <p/>
 * <p>
 * This producer is named, allowing the Project Stage to be accessed via EL:
 * </p>
 * <p/>
 * <pre>
 * #{projectStage}
 * </pre>
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class ProjectStageProducer {
    @Named
    @Produces
    public ProjectStage getProjectStage(final FacesContext context) {
        return context.getApplication().getProjectStage();
    }
}
