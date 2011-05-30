package org.jboss.seam.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

/**
 * <strong>UIFacetInputContainer</strong> is a component to replace UIInputContainer while JSF Bugs:
 * http://java.net/jira/browse/JAVASERVERFACES-1991 and http://java.net/jira/browse/JAVASERVERFACES-2040 are not fixed. This
 * component will probably be deprecated after those bugfixes.
 * 
 * <p>
 * Composite component definition example (minus layout):
 * </p>
 * 
 * <pre>
 * &lt;cc:interface componentType="org.jboss.seam.faces.InputContainer"/>
 * &lt;cc:implementation>
 *   &lt;h:outputLabel id="label" value="#{cc.attrs.label}:" styleClass="#{cc.attrs.invalid ? 'invalid' : ''}">
 *     &lt;h:ouputText styleClass="required" rendered="#{cc.attrs.required}" value="*"/>
 *   &lt;/h:outputLabel>
 *   &lt;cc:renderFacet name="inputs" />
 *   &lt;h:message id="message" errorClass="invalid message" rendered="#{cc.attrs.invalid}"/>
 * &lt;/cc:implementation>
 * </pre>
 * 
 * <p>
 * Composite component usage example:
 * </p>
 * 
 * <pre>
 * &lt;example:facetInputContainer id="name">
 *  &lt;f:facet name="inputs">
 *   &lt;h:inputText id="input" value="#{person.name}"/>
 *  &lt;/f:facet>
 * &lt;/example:facetInputContainer>
 * </pre>
 * 
 * <p>
 * Possible enhancements:
 * </p>
 * <ul>
 * <li>append styleClass "invalid" to label, inputs and messages when invalid</li>
 * </ul>
 * 
 * <p>
 * NOTE: Firefox does not properly associate a label with the target input if the input id contains a colon (:), the default
 * separator character in JSF. JSF 2 allows developers to set the value via an initialization parameter (context-param in
 * web.xml) keyed to javax.faces.SEPARATOR_CHAR. We recommend that you override this setting to make the separator an underscore
 * (_).
 * </p>
 * 
 * @author Dan Allen
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 */
@FacesComponent(UIFacetInputContainer.COMPONENT_TYPE)
public class UIFacetInputContainer extends AbstractUIInputContainer {
    /**
     * The standard component type for this component.
     */
    public static final String COMPONENT_TYPE = "org.jboss.seam.faces.FacetInputContainer";

    @Override
    protected void postScan(FacesContext context, InputContainerElements elements) {
        scan(getFacet("inputs"), elements, context);
    }

}
