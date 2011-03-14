package org.jboss.seam.faces.validation;

import java.io.Serializable;

import javax.faces.component.UIInput;

import org.jboss.seam.solder.core.Veto;

/**
 * To be used in conjunction with <code>&lt;s:validateForm /&gt;</code> in
 * Validators that should have their values fetched from a JSF form field.
 * 
 * Example of injection:
 * 
 * @Inject
 * private InputElement<String> firstName;
 *
 * @Inject
 * private InputElement<String> lastName;
 * 
 * @Inject
 * private InputElement<Date> dateOfBirth;
 * 
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 *
 * @param <T> is the type of value the inputElement holds.
 */
@Veto
public class InputElement<T> implements Serializable{
	
	private String id;
	private String clientId;
	private Object value;
	private UIInput component;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getClientId() {
		return clientId;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public T getValue() {
		return (T)value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public UIInput getComponent() {
		return component;
	}
	
	public void setComponent(UIInput component) {
		this.component = component;
	}
	

}