package org.jboss.seam.faces.view.action;

/**
 * Interface encapsulating view action implementation.
 * 
 * The implementation can be :
 * <ul>
 * <li> a viewController method call.</li>
 * <li> an annotated ViewActionBindingType method call.</li>  
 * <li> an El contained in a ViewAction annotation.</li>
 * <li> ... or any other logic...</li>  
 * </ul>
 * 
 * @author Adriàn Gonzalez
 */
public interface ViewActionStrategy {
    public Object execute();
}
