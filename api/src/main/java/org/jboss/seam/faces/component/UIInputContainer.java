package org.jboss.seam.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

/**
 * <strong>UIInputContainer</strong> is a supplemental component for a JSF 2.0 composite component encapsulating one or more
 * input components (<strong>EditableValueHolder</strong>), their corresponding message components (<strong>UIMessage</strong>)
 * and a label (<strong>HtmlOutputLabel</strong>). This component takes care of wiring the label to the first input and the
 * messages to each input in sequence. It also assigns two implicit attribute values, "required" and "invalid" to indicate that
 * a required input field is present and whether there are any validation errors, respectively. To determine if a input field is
 * required, both the required attribute is consulted and whether the property has Bean Validation constraints. Finally, if the
 * "label" attribute is not provided on the composite component, the label value will be derived from the id of the composite
 * component, for convenience.
 * <p/>
 * <p>
 * Composite component definition example (minus layout):
 * </p>
 * <p/>
 * 
 * <pre>
 * &lt;cc:interface componentType="org.jboss.seam.faces.InputContainer"/>
 * &lt;cc:implementation>
 *   &lt;h:outputLabel id="label" value="#{cc.attrs.label}:" styleClass="#{cc.attrs.invalid ? 'invalid' : ''}">
 *     &lt;h:ouputText styleClass="required" rendered="#{cc.attrs.required}" value="*"/>
 *   &lt;/h:outputLabel>
 *   &lt;cc:insertChildren/>
 *   &lt;h:message id="message" errorClass="invalid message" rendered="#{cc.attrs.invalid}"/>
 * &lt;/cc:implementation>
 * </pre>
 * <p/>
 * <p>
 * Composite component usage example:
 * </p>
 * <p/>
 * 
 * <pre>
 * &lt;example:inputContainer id="name">
 *   &lt;h:inputText id="input" value="#{person.name}"/>
 * &lt;/example:inputContainer>
 * </pre>
 * <p/>
 * <p>
 * Possible enhancements:
 * </p>
 * <ul>
 * <li>append styleClass "invalid" to label, inputs and messages when invalid</li>
 * </ul>
 * <p/>
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
@FacesComponent(UIInputContainer.COMPONENT_TYPE)
public class UIInputContainer extends AbstractUIInputContainer {
    /**
     * The standard component type for this component.
     */
    public static final String COMPONENT_TYPE = "org.jboss.seam.faces.InputContainer";

    @Override
    protected void postScan(FacesContext context, InputContainerElements elements) {
    }

}
