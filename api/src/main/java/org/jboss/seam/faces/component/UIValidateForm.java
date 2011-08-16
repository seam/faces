/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.faces.component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.jboss.seam.solder.logging.Logger;
import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@FacesComponent(UIValidateForm.COMPONENT_TYPE)
public class UIValidateForm extends UIInput {

    private Logger log = Logger.getLogger(UIValidateForm.class);

    private static final AnnotationLiteral<Before> BEFORE = new AnnotationLiteral<Before>() {
        private static final long serialVersionUID = 7631699535063526392L;
    };
    private static final AnnotationLiteral<After> AFTER = new AnnotationLiteral<After>() {
        private static final long serialVersionUID = -929128236303355107L;
    };

    public static final String COMPONENT_TYPE = "org.jboss.seam.faces.ValidateForm";
    public static final String COMPONENT_FAMILY = "org.jboss.seam.faces.ValidateForm";
    private static final String VALIDATOR_ID_KEY = COMPONENT_TYPE + "_ID_KEY";
    private static final Serializable COMPONENTS_MAP_KEY = COMPONENT_TYPE + "_COMPONENTS_MAP_KEY";
    private static final Serializable FIELDS_KEY = COMPONENT_TYPE + "_FIELDS_KEY";
    private static final Serializable SHOW_FIELD_MESSAGES_KEY = COMPONENT_TYPE + "_SHOW_FIELD_MESSAGES";
    private static final Serializable SHOW_GLOBAL_MESSAGES_KEY = COMPONENT_TYPE + "_SHOW_GLOBAL_MESSAGES";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    @Override
    public String getRendererType() {
      return null;
    }

    @Override
    public void validate(final FacesContext context) {
        context.getApplication().publishEvent(context, PreValidateEvent.class, UIValidateForm.class, this);
        BeanManager manager = new BeanManagerLocator().getBeanManager();
        manager.fireEvent(this, BEFORE);

        Validator validator = null;
        try {
            validator = context.getApplication().createValidator(getValidatorId());
            if (validator == null) {
                throw new IllegalArgumentException("Seam UIValidateForm - Could not create Validator with id: ["
                        + getValidatorId() + "]");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Seam UIValidateForm - Could not create validator with id [" + getValidatorId()
                    + "] because: nested exception is:" + e.getMessage(), e);
        }

        Map<String, UIInput> components = getComponents();
        try {
            UIComponent parent = this.getParent();
            validator.validate(context, parent, components);
        } catch (ValidatorException e) {
            setValid(false);
            for (UIInput comp : components.values()) {
                comp.setValid(false);
                if (isShowFieldMessages()) {
                    context.addMessage(comp.getClientId(), e.getFacesMessage());
                }
            }
            if (isShowGlobalMessages()) {
                context.addMessage(null, e.getFacesMessage());
            }
            if(!isShowGlobalMessages() && !isShowFieldMessages()) {
                log.warn("The form validation failed but neither 'showFieldMessages' nor 'showGlobalMessages' " +
                        "is true. The validation messages will be dropped.");
            }
        }

        manager.fireEvent(this, AFTER);
        context.getApplication().publishEvent(context, PostValidateEvent.class, UIValidateForm.class, this);
    }

    /**
     * Attempt to locate the form in which this component resides. If the component is not within a UIForm tag, throw an
     * exception.
     */
    public UIForm locateForm() {
        UIComponent parent = this.getParent();
        while (!(parent instanceof UIForm)) {
            if ((parent == null) || (parent instanceof UIViewRoot)) {
                throw new IllegalStateException(
                        "The UIValidateForm (<s:validateForm />) component must be placed within a UIForm (<h:form>)");
            }
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }

    /*
     * Prevent any rendered output.
     */

    @Override
    public void encodeAll(final FacesContext context) throws IOException {
        locateForm();
    }

    @Override
    public void encodeBegin(final FacesContext context) throws IOException {
    }

    @Override
    public void encodeEnd(final FacesContext context) throws IOException {
    }

    @Override
    public void encodeChildren(final FacesContext context) throws IOException {
    }

    /*
     * Getters & Setters
     */

    public String getFields() {
        StateHelper helper = this.getStateHelper(true);
        return (String) helper.get(FIELDS_KEY);
    }

    public void setFields(final String fields) {
        StateHelper helper = this.getStateHelper(true);
        helper.put(FIELDS_KEY, fields);
    }

    public String getValidatorId() {
        StateHelper helper = this.getStateHelper(true);
        return (String) helper.get(VALIDATOR_ID_KEY);
    }

    public void setValidatorId(final String validatorId) {
        StateHelper helper = this.getStateHelper(true);
        helper.put(VALIDATOR_ID_KEY, validatorId);
    }

    public boolean isShowFieldMessages() {
        StateHelper helper = this.getStateHelper(true);
        return (Boolean) helper.eval(SHOW_FIELD_MESSAGES_KEY, false);
    }

    public void setShowFieldMessages(final boolean showFieldMessages) {
        StateHelper helper = this.getStateHelper(true);
        helper.put(SHOW_FIELD_MESSAGES_KEY, showFieldMessages);
    }

    public boolean isShowGlobalMessages() {
        StateHelper helper = this.getStateHelper(true);
        return (Boolean) helper.eval(SHOW_GLOBAL_MESSAGES_KEY, true);
    }

    public void setShowGlobalMessages(final boolean showGlobalMessages) {
        StateHelper helper = this.getStateHelper(true);
        helper.put(SHOW_GLOBAL_MESSAGES_KEY, showGlobalMessages);
    }

    @SuppressWarnings("unchecked")
    private Map<String, UIInput> getComponents() {
        StateHelper helper = this.getStateHelper(true);
        return (Map<String, UIInput>) helper.get(COMPONENTS_MAP_KEY);
    }

    public void setComponents(final Map<String, UIInput> components) {
        StateHelper helper = this.getStateHelper(true);
        helper.put(COMPONENTS_MAP_KEY, components);
    }
}
