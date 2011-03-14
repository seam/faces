package org.jboss.seam.faces.validation;

import java.io.Serializable;

import javax.faces.component.UIInput;

import org.jboss.seam.solder.core.Veto;

/**
 * 
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 *
 * @param <T>
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