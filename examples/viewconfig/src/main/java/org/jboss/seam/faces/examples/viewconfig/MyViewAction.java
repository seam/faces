package org.jboss.seam.faces.examples.viewconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.view.action.ViewActionBindingType;

@ViewActionBindingType
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface MyViewAction {
	MyAppViewConfig.Pages value();

	//just testing value override
//	public PhaseIdType phase() default PhaseIdType.RENDER_RESPONSE;
//    public Boolean immediate = null;
//    public Boolean onPostback = false;
//    public String condition = null;
}
