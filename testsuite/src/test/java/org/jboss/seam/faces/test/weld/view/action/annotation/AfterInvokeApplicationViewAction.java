package org.jboss.seam.faces.test.weld.view.action.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.view.action.ViewActionBindingType;

@ViewActionBindingType
@After
@InvokeApplication
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface AfterInvokeApplicationViewAction {
    ViewConfigEnum.Pages value();
}
