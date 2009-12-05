package org.jboss.seam.faces.lifecycle;

/**
 * A <strong>FacesSystemEventProcessor</strong> is an object which performs
 * processing when a JSF SystemEvent is fired. Each processor is typically part
 * of a chain. The execute() method returns a boolean indicating whether to
 * continue or abort processing.
 * 
 * @author Dan Allen
 */
public interface FacesSystemEventProcessor
{
   /**
    * Perform processing.
    * 
    * @return Whether the processing should continue. true = continue; false = abort
    */
   public abstract boolean execute();
}
